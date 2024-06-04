package ru.darek.application.processors;

import ru.darek.HttpRequest;
import ru.darek.processors.RequestProcessor;
import ru.darek.processors.RequestProcessorType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HelloWorldRequestProcessor implements RequestProcessor, RequestProcessorType {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        // CRLF
        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nETag: \"beli-berda-12343\"\r\nCache-Control: max-age=10\r\n\r\n<html><body><h1>Hello World!!!</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String headerType() {
        return "text/html";
        //  return "application/json";
    }
}
