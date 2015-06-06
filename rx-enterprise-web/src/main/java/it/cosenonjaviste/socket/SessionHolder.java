package it.cosenonjaviste.socket;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pizzo on 26/05/15.
 */
@ApplicationScoped
public class SessionHolder {

    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void add(Session session) {
        sessions.put(session.getId(), session);
    }

    public void remove(Session session) {
        sessions.remove(session.getId());
    }

    public Optional<Session> get(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }
}
