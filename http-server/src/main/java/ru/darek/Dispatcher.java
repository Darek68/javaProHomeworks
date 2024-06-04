package ru.darek;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.darek.application.processors.*;
import ru.darek.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Dispatcher {
    private Map<String, RequestProcessor> router;
    private RequestProcessor unknownOperationRequestProcessor;
    private RequestProcessor methodNotAllowedProcessor;
    private RequestProcessor notAcceptableProcessor;
    private RequestProcessor optionsRequestProcessor;
    private RequestProcessor staticResourcesProcessor;

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class.getName());

    public Dispatcher() {
        this.router = new HashMap<>();
        this.router.put("GET /calc", new CalculatorRequestProcessor());
        this.router.put("GET /hello", new HelloWorldRequestProcessor());
        this.router.put("GET /items", new GetAllProductsProcessor());
        this.router.put("POST /items", new CreateNewProductProcessor());

        this.unknownOperationRequestProcessor = new DefaultUnknownOperationProcessor();
        this.optionsRequestProcessor = new DefaultOptionsProcessor();
        this.staticResourcesProcessor = new DefaultStaticResourcesProcessor();
        this.notAcceptableProcessor = new DefaultNotAcceptableProcessor();
        this.methodNotAllowedProcessor = new DefaultMethodNotAllowedProcessor();

        logger.info("Диспетчер проинициализирован");
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        String typeRec;
        String typeProc;
        if (httpRequest.getMethod() == HttpMethod.OPTIONS) {
            optionsRequestProcessor.execute(httpRequest, outputStream);
            return;
        }
        if (Files.exists(Paths.get("static/", httpRequest.getUri().substring(1)))) {
            staticResourcesProcessor.execute(httpRequest, outputStream);
            return;
        }


        if (!router.containsKey(httpRequest.getRouteKey())) {
            String uri = "";
            String[] elements = httpRequest.getRouteKey().trim().split(" ");
            if (elements.length > 1) uri = elements[1];
            Iterator<Map.Entry<String, RequestProcessor>> iterator = router.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, RequestProcessor> entry = iterator.next();
                String key = entry.getKey();
                if (key.contains(uri)) {
                    logger.info("\nuri: {}\nkey: {}", uri, key);
                    methodNotAllowedProcessor.execute(httpRequest, outputStream);
                    return;
                }
            }
            unknownOperationRequestProcessor.execute(httpRequest, outputStream);
            return;
        }
        typeProc = ((RequestProcessorType) router.get(httpRequest.getRouteKey())).headerType();
        typeRec = httpRequest.getHeaderValue("Accept");
        if (! typeRec.contains(typeProc)) {
            notAcceptableProcessor.execute(httpRequest, outputStream);
            return;
        }
        router.get(httpRequest.getRouteKey()).execute(httpRequest, outputStream);
    }
}
