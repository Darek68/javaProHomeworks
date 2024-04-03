package ru.darek;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Task {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private Status status;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}



