package ru.darek;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class MyThreadPool {
    public static final Logger logger = LogManager.getLogger(MyThreadPool.class.getName());
    Thread[] threads;
    List<Runnable> list = new LinkedList<>();
    boolean shutdown;
    int pause;

    public MyThreadPool(int count) {
        this(count, 1000);
    }

    public MyThreadPool(int count, int pause) {
        this.shutdown = false;
        this.pause = pause;
        this.threads = new Thread[count];
        startOfTreads();
        System.out.println("Конец конструктора!");
        logger.debug("Конструктор отработал!");
    }

    private void startOfTreads() {
        for (int i = 0; i < threads.length; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Runnable r;
                    while (!shutdown) {
                        logger.debug(Thread.currentThread().getName() + " Ready");
                        sleep();
                        r = nextTask();
                        if (r != null) r.run();
                        else logger.debug(Thread.currentThread().getName() + " Resting");
                    }
                }
            }).start();
        }
    }

    public synchronized int execute(Runnable r) {
        if (shutdown) throw new IllegalStateException("MyThreadPool is shutdown!");
        list.add(r);
        return list.size();
    }

    private synchronized Runnable nextTask() {
        if (list.isEmpty()) return null;
        Runnable result = list.get(0);
        list.remove(0);
        return result;
    }

    public boolean shutdown() {
        if (!this.shutdown) this.shutdown = true;
        return this.shutdown;
    }

    public void sleep() {
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

