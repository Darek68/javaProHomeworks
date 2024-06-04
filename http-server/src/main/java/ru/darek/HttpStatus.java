package ru.darek;

public enum HttpStatus {
    OK(200, "OK"),
    NO_CONTENT(204, "No Content");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
