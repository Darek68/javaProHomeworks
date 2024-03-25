package ru.darek;

import ru.darek.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

public class TestsOfClass {
    private static Method mBeforeSuite;
    private static Method mAfterSuite;
    private static int successTests;
    private static int crashTests;
    private static Set<Method> mSetBefore = new HashSet<>() ;
    private static Set<Method> mSetAfter = new HashSet<>();
    private static Set<Method> mSetUsedMethod = new HashSet<>();

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
        if (c.isAnnotationPresent(Disabled.class))
            return String.format("Класс %s отключен от тестов аннотацией Disabled", c.getName());
        String checkSuite = CheckSuite(c);
        if (checkSuite != null) return checkSuite;
        if (mBeforeSuite != null) {
            try {
                //  mBeforeSuite.invoke(null);
                runMethod(mBeforeSuite);
            } catch (Exception e) {
                crashTests++;
            }
          //  successTests++;
            System.out.println("------------------------------");
        }
        for (int i = 10; i > 0; i--) {
            for (Method m : c.getDeclaredMethods()) {
                if (!mBeforeSuite.equals(m) && !mAfterSuite.equals(m) && !mSetBefore.contains(m) && !mSetAfter.contains(m)) {
                    if (m.isAnnotationPresent(Test.class) && m.getAnnotation(Test.class).priority() == i) {
                        try {
                            //  m.invoke(null);
                            if (!checkDisabled(m)) {
                                runMethod(m);
                            }
                        } catch (Exception e) {
                            crashTests++;
                        }
                     //   successTests++;
                        System.out.println("-----------");
                    }
                }
            }
        }
        if (mAfterSuite != null) {
            System.out.println("------------------------------");
            try {
                //  mAfterSuite.invoke(null);
                runMethod(mAfterSuite);
            } catch (Exception e) {
                crashTests++;
            }
           // successTests++;
        }
        return String.format("Всего тестов %d, успешных %d, провальных %d", crashTests + successTests, successTests, crashTests);
    }

    private static void runMethod(Method m) throws Exception {
        for (Method mBefore : mSetBefore) {
            mBefore.invoke(null);
            if (! mSetUsedMethod.contains(mBefore)){
                successTests++;
                mSetUsedMethod.add(mBefore);
            }
        }
        m.invoke(null);
        if (! mSetUsedMethod.contains(m)){
            successTests++;
            mSetUsedMethod.add(m);
        }
        for (Method mAfter : mSetAfter) {
            mAfter.invoke(null);
            if (! mSetUsedMethod.contains(mAfter)){
                successTests++;
                mSetUsedMethod.add(mAfter);
            }
        }
    }

    private static boolean checkDisabled(Method m) {
        return m.isAnnotationPresent(Disabled.class);
    }

    private static String CheckSuite(Class c) {
        int countOfAnnBefor = 0;
        int countOfAnnAfter = 0;
        for (Method m : c.getDeclaredMethods()) {
            if (!checkDisabled(m)) {
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
                if (m.isAnnotationPresent(Before.class)) {
                    if (m.isAnnotationPresent(Test.class)) mSetBefore.add(m);
                    else return "Аннотация Before должна быть вместе с Test";
                }
                if (m.isAnnotationPresent(After.class)) {
                    if (m.isAnnotationPresent(Test.class)) mSetAfter.add(m);
                    else return "Аннотация After должна быть вместе с Test";
                }
            }
        }
        System.out.println("BeforeSuite: " + mBeforeSuite.getName() + "  AfterSuite: " + mAfterSuite.getName());
        System.out.println("Before: " + mSetBefore.toString() + "  After: " + mSetAfter.toString());
        return null;
    }
}
