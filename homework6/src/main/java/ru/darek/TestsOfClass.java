package ru.darek;

import ru.darek.annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestsOfClass {
    public static void RunTestsOfClass(String name) {
        Class c;
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException e) {
            System.out.println("Не найден класс " + name);
            return;
        }
        RunTestsOfClass(c);
    }

    public static void RunTestsOfClass(Class c) {
        System.out.println("Старт!");
        for (int i = 10; i > 0; i--) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Test.class) && m.getAnnotation(Test.class).priority() == i) {
                    System.out.println(m.getName() + " " + m.getAnnotation(Test.class).priority() + " >>> " + i);
                    try {
                        m.invoke(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
