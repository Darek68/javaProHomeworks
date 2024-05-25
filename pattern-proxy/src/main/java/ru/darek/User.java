package ru.darek;

public class User {
    private String name;

    private int age;
    private double balance;
    private boolean sex;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isSex() {
        return sex;
    }

    public User(String name, int age, double balance, boolean sex) {
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.sex = sex;
    }
}

