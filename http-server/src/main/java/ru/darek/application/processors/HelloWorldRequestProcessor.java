package ru.darek.application.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.darek.HttpRequest;
import ru.darek.processors.RequestProcessor;
import ru.darek.processors.RequestProcessorType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HelloWorldRequestProcessor implements RequestProcessor, RequestProcessorType {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldRequestProcessor.class);
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String cookie = httpRequest.getSessionId();
        logger.info("cookie {}", cookie);
        // CRLF
        String response = "HTTP/1.1 200 OK" + cookie + "\r\nContent-Type: text/html\r\nETag: \"beli-berda-12343\"\r\nCache-Control: max-age=10\r\n\r\n<html><body><h1>Hello World!!!</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String headerType() {
        return "text/html";
        //  return "application/json";
    }
}
