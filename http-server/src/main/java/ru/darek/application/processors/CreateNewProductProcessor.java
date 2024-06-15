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

public class CreateNewProductProcessor implements RequestProcessor, RequestProcessorType {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Item item = gson.fromJson(httpRequest.getBody(), Item.class);
        Storage.save(item);
        String jsonOutItem = gson.toJson(item);

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Connection: keep-alive\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "\r\n" + jsonOutItem;
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String headerType() {
        return "application/json";
    }
}
