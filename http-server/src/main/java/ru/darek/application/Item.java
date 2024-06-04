package ru.darek.application;

import java.util.UUID;

public class Item {
    private UUID id;
    private String title;
    private int price;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Item() {
    }

    public Item(String title, int price) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.price = price;
    }
}
