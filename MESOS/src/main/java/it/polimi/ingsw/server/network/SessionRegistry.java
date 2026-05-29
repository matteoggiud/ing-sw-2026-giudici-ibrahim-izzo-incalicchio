package it.polimi.ingsw.server.network;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        if (session == null || session.getNickname() == null) {
            throw new IllegalArgumentException("Session and session nickname cannot be null");
        }

        String nickname = session.getNickname();

        // verifying if the nickname is already associated with an active session
        ClientSession existingSession = loggedSessions.get(nickname);
        if (existingSession != null && existingSession != session) {
            // If the existing session is reachable, it means the nickname is already connected from another session
            try {
                if (existingSession.getEventHandler().getView().isReachable()) {
                    throw new RuntimeException("Nickname is already connected from another session. Please logout first.");
                }
            } catch (RuntimeException e) {
                throw e; // Rethrow the exception to be handled by the caller
            } catch (Exception e) {
                // Se isReachable() fallisce, significa che la sessione è stale, disconnettila
                System.out.println("[INFO] Detected stale session for " + nickname + ", force disconnecting");
                try {
                    existingSession.disconnectSession();
                } catch (Exception ex) {
                    System.err.println("[WARN] Error disconnecting stale session: " + ex.getMessage());
                }
            }
            // Remove the stale session from the registry
            loggedSessions.remove(nickname);
        }

        loggedSessions.put(nickname, session);
    }

    public synchronized void clearActiveGameIdForGame(int gameId) {
        for (ClientSession session : loggedSessions.values()) {
            if (session.getActiveGameId() != null && session.getActiveGameId().equals(gameId)) {
                session.clearActiveGameId();
            }
        }
    }


    public synchronized void unregisterSession(ClientSession session) {
        if (session == null || session.getNickname() == null) {
            throw new IllegalArgumentException("Session and session nickname cannot be null");
        }
        loggedSessions.remove(session.getNickname());
    }

    public synchronized ClientSession getSession(String nickname) {
        return loggedSessions.get(nickname);
    }

    /**
     * Provides a snapshot of the current active sessions. This method returns a new list containing the current sessions,
     * @return a list of active ClientSession instances. Modifications to this list will not affect the internal state of the SessionRegistry.
     */
   public synchronized List<ClientSession> getSessionsSnapshot() {
        return new ArrayList<>(loggedSessions.values());
    }
}
