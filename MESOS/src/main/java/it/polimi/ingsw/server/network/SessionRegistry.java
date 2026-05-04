package it.polimi.ingsw.server.network;

import java.util.HashMap;
import java.util.Map;

public final class SessionRegistry {
    private static final SessionRegistry INSTANCE = new SessionRegistry();
    private final Map<String, ClientSession> loggedSessions = new HashMap<>();

    private SessionRegistry() {
    }

    public static SessionRegistry getInstance() {
        return INSTANCE;
    }

    public synchronized void registerSession(ClientSession session) {
        if(session == null || session.getNickname() == null) {
            throw new IllegalArgumentException("Session and session nickname cannot be null");
        }
        loggedSessions.put(session.getNickname(), session);
    }

    public synchronized void unregisterSession(ClientSession session) {
        if(session == null || session.getNickname() == null) {
            throw new IllegalArgumentException("Session and session nickname cannot be null");
        }
        loggedSessions.remove(session.getNickname());
    }
    public synchronized ClientSession getSession(String nickname) {
        return loggedSessions.get(nickname);
    }
}
