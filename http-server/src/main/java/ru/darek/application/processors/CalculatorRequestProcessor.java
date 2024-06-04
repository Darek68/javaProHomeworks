package ru.darek.application.processors;

import ru.darek.HttpRequest;
import ru.darek.processors.RequestProcessor;
import ru.darek.processors.RequestProcessorType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor, RequestProcessorType {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        int a = Integer.parseInt(httpRequest.getParameter("a"));
        int b = Integer.parseInt(httpRequest.getParameter("b"));
        int result = a + b;
        String outMessage = a + " + " + b + " = " + result;

        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>" + outMessage + "</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String headerType() {
        return "text/html";
    }
}
