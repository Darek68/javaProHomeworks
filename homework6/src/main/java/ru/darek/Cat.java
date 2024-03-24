package ru.darek;

import ru.darek.annotation.AfterSuite;
import ru.darek.annotation.BeforeSuite;
import ru.darek.annotation.Test;

public class Cat {
    @BeforeSuite
    @Test
    public static void CatIsStart(){
        System.out.println("Кот появился!");
    }
    @Test(priority = 5)
    public static void CatIsGood(){
        System.out.println("Мяууу...");
    }
    @Test(priority = 10)
    public static void CatIsBed(){
        System.out.println("Хррр...");
    }
    @Test
    public static void CatIsHungry(){
        System.out.println("Муррр...");
    }
    @AfterSuite
    @Test
    public static void CatIsGo(){
        System.out.println("Кот уходит.");
    }
}
