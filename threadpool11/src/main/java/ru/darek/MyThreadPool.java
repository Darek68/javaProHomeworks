package ru.darek;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class MyThreadPool {
    public static final Logger logger = LogManager.getLogger(MyThreadPool.class.getName());

    private final Thread[] threads;
    private List<Runnable> list = new LinkedList<>();
    private volatile boolean shutdown;
    int pause;

    public MyThreadPool(int count) {
        this(count, 1000);
    }

    public MyThreadPool(int count, int pause) {
        this.shutdown = false;
        this.pause = pause;
        this.threads = new Thread[count];
        startOfTreads();
        logger.debug("Конструктор отработал!");
    }

    private void startOfTreads() {
        for (int i = 0; i < threads.length; i++) {
            int finalI = i;
            int finalI1 = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threads[finalI1] = Thread.currentThread();
                    Runnable r;
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            r = nextTask();
                            r.run();
                            sleep();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    logger.debug(Thread.currentThread().getName() + " is closed");
                }
            }).start();
        }
    }

    public synchronized int execute(Runnable r) {
        if (shutdown) {
            throw new IllegalStateException("MyThreadPool is shutdown!");
        }
        list.add(r);
        notifyAll();
        return list.size();
    }

    private synchronized Runnable nextTask() throws InterruptedException {
        if (list.isEmpty()) {
            logger.debug(Thread.currentThread().getName() + " is waiting");
            wait();
        }
        Runnable result = list.get(0);
        list.remove(0);
        return result;
    }

    public boolean shutdown() {
        if (!this.shutdown) this.shutdown = true;
        logger.debug(" shutdown()");
        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
        }
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