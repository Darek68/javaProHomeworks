package ru.darek;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyApp {
    public static final Logger logger = LogManager.getLogger(MyApp.class.getName());
    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(3);
        for (int i = 0; i < 7; i++) {
            int finalI = i;
            myThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    logger.info(Thread.currentThread().getName() + " Выполняется задача № " + finalI);
                }
            });
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            myThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    logger.info(Thread.currentThread().getName() + " Выполняется задача № " + (10 + finalI));
                }
            });
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Тормозим пулл?");
        myThreadPool.shutdown();
    }
}