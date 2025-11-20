package teo.chat.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionRegistry {

    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // Register a session
    public void registerSession(WebSocketSession session) {
        sessions.add(session);
    }

    // Unregister a session
    public void unregisterSession(WebSocketSession session) {
        sessions.remove(session);
    }

    // Access all active sessions
    public Set<WebSocketSession> getSessions() {
        return sessions;
    }
}
