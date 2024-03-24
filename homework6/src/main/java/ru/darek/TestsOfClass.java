package ru.darek;

import ru.darek.annotation.AfterSuite;
import ru.darek.annotation.BeforeSuite;
import ru.darek.annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestsOfClass {
    private static Method mBeforeSuite;
    private static Method mAfterSuite;
    private static int successTests;
    private static int crashTests;

    public static String RunTestsOfClass(String name) {
        Class c;
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException e) {
            return "Не найден класс " + name;
        }
        return RunTestsOfClass(c);
    }

    public static String RunTestsOfClass(Class c) {
        String checkSuite = CheckSuite(c);
        if (checkSuite != null) return checkSuite;
        if (mBeforeSuite != null) {
            try {
                mBeforeSuite.invoke(null);
            } catch (Exception e) {
                crashTests++;
            }
            successTests++;
        }
        for (int i = 10; i > 0; i--) {
            for (Method m : c.getDeclaredMethods()) {
                if (!mBeforeSuite.equals(m) && !mAfterSuite.equals(m)) {
                    if (m.isAnnotationPresent(Test.class) && m.getAnnotation(Test.class).priority() == i) {
                        try {
                            m.invoke(null);
                        } catch (Exception e) {
                            crashTests++;
                        }
                        successTests++;
                    }
                }
            }
        }
        if (mAfterSuite != null) {
            try {
                mAfterSuite.invoke(null);
            } catch (Exception e) {
                crashTests++;
            }
            successTests++;
        }
        return String.format("Всего тестов %d, успешных %d, провальных %d", crashTests + successTests, successTests, crashTests);
    }

    private static String CheckSuite(Class c) {
        int countOfAnnBefor = 0;
        int countOfAnnAfter = 0;
        for (Method m : c.getDeclaredMethods()) {
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                countOfAnnBefor++;
                if (countOfAnnBefor > 1) return "Количество аннотации BeforeSuite > 1";
                if (m.isAnnotationPresent(Test.class)) mBeforeSuite = m;
            }
            if (m.isAnnotationPresent(AfterSuite.class)) {
                countOfAnnAfter++;
                if (countOfAnnAfter > 1) return "Количество аннотации AfterSuite > 1";
                if (m.isAnnotationPresent(Test.class)) mAfterSuite = m;
            }
        }
        System.out.println("BeforeSuite: " + mBeforeSuite.getName() + "  AfterSuite: " + mAfterSuite.getName());
        return null;
    }
}
