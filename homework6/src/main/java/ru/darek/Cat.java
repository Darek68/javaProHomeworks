package ru.darek;

import ru.darek.annotation.AfterSuite;
import ru.darek.annotation.BeforeSuite;
import ru.darek.annotation.Test;

public class Cat {
    @BeforeSuite
    @Test
    public static void CatIsStart(){
        System.out.println("BeforeSuite Кот появился!");
    }
    @Test(priority = 5)
    public static void CatIsGood(){
        System.out.println("5 Потом кот хороший...");
    }
    @Test(priority = 10)
    public static void CatIsBed(){
        System.out.println("10 Сперва кот злой...");
    }
    @Test
    public static void CatIsHungry(){
        System.out.println("Defoult Кот голодный...");
    }
    @AfterSuite
    @Test(priority = 10)
    public static void CatIsGo(){
        System.out.println("AfterSuite Кот уходит.");
    }
}
