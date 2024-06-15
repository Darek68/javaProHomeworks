package ru.darek.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.darek.HttpRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SessionHandler.class);
    private static final Long tenMin = (1 * 60 * 1000L); //
    private static final String SESSIONID = "SESSIONID";
    private static Map<String, Date> sessions;
    private String cookies;
    private String sessionId;

    static {
        sessions = new HashMap<>();
    }

    public SessionHandler(String cookies) {
        this.cookies = cookies;
        this.sessionId = getSessionIdFromCookies(this.cookies);
        setSessionId(this.sessionId);
    }

    private void setSessionId(String sessionId) {
        logger.info("sessionId - {} sessions {}   sessions.containsKey(sessionId) {}", this.sessionId,sessions,sessions.containsKey(this.sessionId));
        Date sessionIdDate = null;
        if (this.sessionId == null || !sessions.containsKey(sessionId)) {
            this.sessionId = UUID.randomUUID().toString();
            sessions.put(this.sessionId, new Date());
            logger.info("1sessionId - {} sessions {}   sessions.containsKey(sessionId) {}", this.sessionId,sessions,sessions.containsKey(this.sessionId));
            return;
        }
        sessionIdDate = sessions.get(sessionId);
        logger.info("2sessionIdDate - {}", sessionIdDate);
        if (sessionIdDate == null) {
            sessions.put(sessionId, new Date());
            logger.info("2sessionId - {}", this.sessionId);
            return;
        }
        logger.info("3TimeOfsessionId - {}", new Date().getTime() - sessionIdDate.getTime());
        if ((new Date().getTime() - sessionIdDate.getTime()) > tenMin){
            logger.info("3sessionIdDate - {}", (new Date().getTime() - sessionIdDate.getTime()));
            sessions.remove(sessionId);
            this.sessionId = UUID.randomUUID().toString();
            sessions.put(this.sessionId, new Date());
            logger.info("3sessionId - {}", this.sessionId);
        }
    }

    public String getSessionId() {
        logger.info("return sessionId - {}", this.sessionId);
        return this.sessionId;
    }

    private String getSessionIdFromCookies(String cookies) {
        if (cookies == null) return cookies;
        int start = cookies.indexOf("SESSIONID") + 10;
        int stop = cookies.indexOf(" ", start);
        if (stop == -1) stop = cookies.length();
        logger.info("cookies - {} sessionId - {}", cookies, cookies.substring(start, stop));
        return cookies.substring(start, stop);
    }
}
