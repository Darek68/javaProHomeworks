package ru.darek.processors;

import ru.darek.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {
    void execute(HttpRequest httpRequest, OutputStream output) throws IOException;
}
