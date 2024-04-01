package ru.darek;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.darek.annotation.*;

public class Cat {
    public static final Logger logger = LogManager.getLogger(Cat.class.getName());

    @BeforeSuite
    public static void сatIsStart() { logger.info("BeforeSuite Кот появился!"); }

    @Test(priority = 5)
    public static void сatIsGood() { logger.info("5 Потом кот хороший..."); }

    @Disabled
    @Test(priority = 10)
    public static void сatIsBed() {
        logger.info("10 Сперва кот злой...");
    }

    @Test
    public static void сatIsHungry() {
        logger.info("Defoult Кот голодный...");
    }

    @AfterSuite
    public static void сatIsGo() {
        logger.info("AfterSuite Кот уходит.");
    }

    @After
    @Test(priority = 10)
    public static void сatIsMissing() {
        logger.info("After Кот уснул.");
    }

    @Before
    @Test
    public static void сatIsReady() {
        logger.info("Before Кот проснулся!");
    }

    @ThrowsException
    @Test(priority = 8)
    public static void сatIsException() throws ClassNotFoundException {
        logger.info("8 У кота исключение ClassNotFoundException");
        throw new ClassNotFoundException();
    }

    @ThrowsException
    @Test
    public static void сatIsExceptionOver() {
        logger.info("У кота исключение ArithmeticException");
        throw new ArithmeticException();
    }
}
