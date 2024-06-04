package ru.darek.processors;

import ru.darek.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultNotAcceptableProcessor implements RequestProcessor{
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String response = "HTTP/1.1 406 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>Not Acceptable!!!</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
