package ru.darek;

import java.util.Iterator;
import java.util.List;

public class MyApp {
    public static void main(String[] args) {
        Product product = Product.builder()
                .id(1)
                .title("Чемодан")
                .description("Вместимый,красный")
                .cost(2345.40)
                .weight(3.55)
                .width(23)
                .length(45)
                .height(55)
                .build();

        BoxNew boxNew = new BoxNew(List.of("1", "2", "3"),
                null,
                List.of("1a", "2a", "3a", "4a", "5a", "6a", "7a"),
                List.of("5b", "6b", "7b", "8b"));
        Iterator<String> it = boxNew.getIterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}


