package ru.darek;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {

        Class c2 = Class.forName("ru.darek.Cat");
        Class c1 = Cat.class;

        System.out.println(TestsOfClass.RunTestsOfClass("ru.darek.Cat"));
     //   TestsOfClass.RunTestsOfClass(c1);

    }
}