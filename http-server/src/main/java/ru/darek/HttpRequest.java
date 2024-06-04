package ru.darek;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.*;


public class HttpRequest {
    private String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private static final String CRLF = "\r\n";

    private String sessionId;
    private static final String SESSIONID = "SESSIONID";

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public String getUri() {
        return uri;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getBody() {
        return body;
    }

    public HttpMethod getMethod() {
        return method;
    }
    public String getSessionId() {
        int start, stop;
        String sessionId = getHeaderValue("Cookie");
        if (sessionId!=null && sessionId.contains(SESSIONID)){
          /*  start = sessionId.indexOf("SESSIONID") + 9;
            stop = sessionId.indexOf(" ",start);
            sessionId = sessionId.substring(start,stop);
            if (sessionId==SESSIONID) */ return "";
        }
        return ("\r\nSet-Cookie: SESSIONID=" + this.sessionId);
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parseRequestLine();
        this.tryToParseBody();
        this.sessionId = UUID.randomUUID().toString();
        logger.info("sessionIdUUID {}", sessionId);

        logger.debug("\nall-rawRequest\n{}", rawRequest);
        logger.trace("{} {}\nParameters: {}\nBody: {}", method, uri, parameters, body); // TODO правильно все поназывать
    }

    public void tryToParseBody() {
        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            List<String> lines = rawRequest.lines().collect(Collectors.toList());
            int splitLine = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).isEmpty()) {
                    splitLine = i;
                    break;
                }
            }
            if (splitLine > -1) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = splitLine + 1; i < lines.size(); i++) {
                    stringBuilder.append(lines.get(i));
                }
                this.body = stringBuilder.toString();
            }
        }
    }

    public void parseRequestLine() {
        int start, stop;
        String headerName, headerValue;
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        this.parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                this.parameters.put(keyValue[0], keyValue[1]);
            }
        }
        logger.info("\nall-rawRequest\n{}", rawRequest);
        stop = rawRequest.indexOf(CRLF) + 2;
        do{
            start = stop;
            stop = rawRequest.indexOf(":",start);
            headerName = rawRequest.substring(start,stop).trim();
            start = stop + 2;
            stop = rawRequest.indexOf(CRLF,start);
            headerValue = rawRequest.substring(start,stop).trim();
            logger.info("header={}", headerName + " : " + headerValue);
            headers.put(headerName,headerValue);
            stop += 2;
        } while (stop != rawRequest.indexOf(CRLF,stop));
    }
    public String getHeaderValue(String key) {
        return headers.get(key);
    }
}
