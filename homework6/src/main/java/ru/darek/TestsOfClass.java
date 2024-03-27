package ru.darek;

import ru.darek.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

public class TestsOfClass {
    private static int successTests;
    private static int crashTests;
    private static Set<Method> mSetBefore = new HashSet<>();
    private static Set<Method> mSetAfter = new HashSet<>();
    private static Set<Method> mSetUsedMethod = new HashSet<>();
    private static List<Method>[] lists = new List[12];

    static {
        for (int i = 0; i < 12; i++) {
            lists[i] = new ArrayList<Method>();
        }
    }

    public static String runTestsOfClass(String name) {
        Class c;
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException e) {
            return "Не найден класс " + name;
        }
        return runTestsOfClass(c);
    }

    public static String runTestsOfClass(Class c) {
        if (c.isAnnotationPresent(Disabled.class))
            return String.format("Класс %s отключен от тестов аннотацией Disabled", c.getName());
        String checkSuite = checkMethods(c);
        if (checkSuite != null) return checkSuite;
        for (int i = 11; i >= 0; i--) {
            for (Method m : lists[i]) {
                try {
                    runMethod(m, (i > 0 && i < 11));
                } catch (Exception e) {
                    if (m.isAnnotationPresent(ThrowsException.class) &&
                            e.getCause().toString().contains("ArithmeticException")) {
                        successTests++;
                    } else crashTests++;
                    mSetUsedMethod.add(m);
                }
                System.out.println("-----------");
            }
        }
        return String.format("Всего тестов %d, успешных %d, провальных %d",
                crashTests + successTests,
                successTests, crashTests);
    }

    private static String checkMethods(Class c) {
        String  result = null;
        for (Method m : c.getDeclaredMethods()) {
            result = checkOneMethod(m);
            if (result != null) return result;
        }
        return null;
    }

    private static String checkOneMethod(Method m) {
        if (m.isAnnotationPresent(Disabled.class)) return null;
        if (m.isAnnotationPresent(BeforeSuite.class)) {
            if (!lists[11].isEmpty()) return "Количество аннотации BeforeSuite > 1";
            lists[11].add(m);
        }
        if (m.isAnnotationPresent(AfterSuite.class)) {
            if (!lists[0].isEmpty()) return "Количество аннотации AfterSuite > 1";
            lists[0].add(m);
        }
        if (m.isAnnotationPresent(Before.class)) {
            if (m.isAnnotationPresent(Test.class)) mSetBefore.add(m);
            else return "Аннотация Before должна быть вместе с Test";
        }
        if (m.isAnnotationPresent(After.class)) {
            if (m.isAnnotationPresent(Test.class)) mSetAfter.add(m);
            else return "Аннотация After должна быть вместе с Test";
        }
        if (m.isAnnotationPresent(Test.class) &&
                !m.isAnnotationPresent(Before.class) &&
                !m.isAnnotationPresent(After.class)) {
            lists[m.getAnnotation(Test.class).priority()].add(m);
        }
        return null;
    }

    private static void runMethod(Method m, boolean befoAfter) throws Exception {
        if (befoAfter) {
            for (Method mBefore : mSetBefore) {
                mBefore.invoke(null);
                if (!mSetUsedMethod.contains(mBefore)) {
                    successTests++;
                    mSetUsedMethod.add(mBefore);
                }
            }
        }
        m.invoke(null);
        if (!mSetUsedMethod.contains(m)) {
            successTests++;
            mSetUsedMethod.add(m);
        }
        if (befoAfter) {
            for (Method mAfter : mSetAfter) {
                mAfter.invoke(null);
                if (!mSetUsedMethod.contains(mAfter)) {
                    successTests++;
                    mSetUsedMethod.add(mAfter);
                }
            }
        }
    }
}
