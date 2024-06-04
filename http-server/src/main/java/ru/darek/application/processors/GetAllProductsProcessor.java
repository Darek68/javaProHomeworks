package ru.darek.application.processors;

import com.google.gson.Gson;
import ru.darek.HttpRequest;
import ru.darek.application.Item;
import ru.darek.application.Storage;
import ru.darek.processors.RequestProcessor;
import ru.darek.processors.RequestProcessorType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllProductsProcessor implements RequestProcessor, RequestProcessorType {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        List<Item> items = Storage.getItems();
        Gson gson = new Gson();
        String result = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Connection: keep-alive\r\n" +
                "Access-Control-Allow-Origin: *\r\n\r\n" + gson.toJson(items);
        output.write(result.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    @Override
    public String headerType() {
        return "application/json";
    }
}
