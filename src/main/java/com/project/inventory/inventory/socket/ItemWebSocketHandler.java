package com.project.inventory.inventory.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Component
public class ItemWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void broadcastUpdate(String action,Object itemDTO) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", action);
        payload.put("object", itemDTO);
        String json = objectMapper.writeValueAsString(payload);

        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                session.sendMessage(new TextMessage(json));
            }
        }
    }
}
