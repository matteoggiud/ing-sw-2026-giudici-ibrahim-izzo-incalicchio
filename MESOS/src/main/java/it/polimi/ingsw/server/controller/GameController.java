package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.menu.GameManager;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.LeadBoardDTO;
import it.polimi.ingsw.server.network.ClientSession;
import it.polimi.ingsw.server.network.SessionRegistry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final GamePortal model;

    /**
     * Constructs a GameController with the specified GamePortal instance.
     *
     * @param model the GamePortal instance that serves as the main entry point for interacting with the game model
     */
    public GameController(GamePortal model) {
        this.model = model;
    }

    /**
     * Allows a player to log in to the game using their username and passwordHash
     *
     * @param clientSession the client session of the player
     * @param username      the username of the player attempting to log in
     * @param password      the passwordHash of the player attempting to log in
     */
    public void login(ClientSession clientSession, String username, String password) throws RuntimeException {
        if (clientSession.isLoggedIn()) {
            throw new RuntimeException("You are already logged in, please logout first.");
        }
        disconnectStaleSessionIfPresent(username);

        try {
            model.login(username, password);
            clientSession.setNickname(username);

            // Register the client session in the session registry to allow for proper session management and cleanup on disconnect
            SessionRegistry.getInstance().registerSession(clientSession);

            System.out.println("[INFO] Player " + username + " logged in successfully.");
            try {
                Integer gameId = model.getActiveGameIdForPlayer(username);
                if (gameId != null) {
                    clientSession.setActiveGameId(gameId);
                    model.reconnectToGame(gameId, username, clientSession.getEventHandler());
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Exception caught in GameController (reconnecting to game): " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in GameController (login): " + e.getMessage());
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }


    /**
     * Allows a new player to register an account using a username and passwordHash
     *
     * @param clientSession the client session of the player
     * @param username      the desired username for the new player account
     * @param password      the desired passwordHash for the new player account
     */
    public void register(ClientSession clientSession, String username, String password) throws RuntimeException {
        if (clientSession.isLoggedIn()) {
            throw new RuntimeException("You are already logged in, please logout first.");
        }

        disconnectStaleSessionIfPresent(username);

        try {
            model.register(username, password);

            // Automatically log in the player after successful registration
            clientSession.setNickname(username);
            // Register the client session in the session registry to allow for proper session management and cleanup on disconnect
            SessionRegistry.getInstance().registerSession(clientSession);
            System.out.println("[INFO] Player " + username + " register and logged in successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in GameController (register): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Register failed: " + e.getMessage(), e);
        }
    }

    /**
     * Allows a player to join an existing game using the game's unique identifier and the player's username
     *
     * @param clientSession the client session of the player
     * @param gameId        the unique identifier of the game the player wishes to join
     */
    public void joinGame(ClientSession clientSession, int gameId) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to join a game.");
        }
        // refreshActiveGameIdFromModel(clientSession);
        if (clientSession.isInGame()) {
            throw new IllegalStateException("Player is already in a game and cannot join another.");
        }

        model.joinGame(gameId, clientSession.getNickname(), clientSession.getEventHandler());


        // update the client session to reflect the game they have joined
        clientSession.setActiveGameId(gameId);

        System.out.println("[INFO] Player " + clientSession.getNickname() + " joined game " + gameId + " successfully.");

        model.startGameIfReady(gameId);

    }

    /**
     * Allows a player to leave a game they have joined using the game's unique identifier and the player's username
     * It also disconnects the player's virtual view from the game they have left and updates the client session
     *
     * @param clientSession the client session of the player
     * @return a list of remaining player nicknames in the game after the player has left, which can be used to update the views of the remaining players and inform them about the change in the game state
     * @throws IllegalStateException if the player is not logged in or not currently in a game
     */
    public List<ClientSession> leaveGame(ClientSession clientSession) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to leave a game.");
        }
        if (!clientSession.isInGame()) {
            throw new IllegalStateException("Player is not currently in a game and cannot leave it.");
        }

        // Update the client session to reflect that the player has left the game
        List<String> remainingPlayerNicknames = model.leaveGame(clientSession.getActiveGameId(), clientSession.getNickname());

        clientSession.clearActiveGameId();

        List<ClientSession> remainingPlayerSessions = new ArrayList<>();
        for (String player : remainingPlayerNicknames) {
            ClientSession playerSession = SessionRegistry.getInstance().getSession(player);
            if (playerSession == null)
                continue;

            remainingPlayerSessions.add(playerSession);
            // The game is ended so we need to clear the active game id for all the remaining players in the game as well
            playerSession.clearActiveGameId();
        }

        return remainingPlayerSessions;
    }

    /**
     * Allows a player to log out of the game, ending their session and disconnecting them from the server
     *
     * @param clientSession the client session of the player
     */
    public void logout(ClientSession clientSession) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to logout.");
        }
        if (clientSession.isInGame()) {
            throw new IllegalStateException("Player is currently in a game and cannot logout.");
        }

        // Unregister the client session from the session registry to ensure proper cleanup and prevent memory leaks
        SessionRegistry.getInstance().unregisterSession(clientSession);
        model.logout(clientSession.getNickname());
    }

    /**
     * Allows a player to create a new game with a specified expected player count, which they will host
     *
     * @param clientSession       the client session of the player
     * @param expectedPlayerCount the expected number of players for the game being created
     */
    public void createGame(ClientSession clientSession, int expectedPlayerCount) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to create a game.");
        }
        // refreshActiveGameIdFromModel(clientSession);
        if (clientSession.isInGame()) {
            throw new IllegalStateException("Player is currently in a game and cannot create another.");
        }

        int gameId = model.createGame(clientSession.getNickname(), expectedPlayerCount, clientSession.getEventHandler());
        clientSession.setActiveGameId(gameId);

    }


    /**
     * Allows a player to draw a card from the game
     *
     * @param clientSession  the client session of the player
     * @param selectedCardId the card that the player has selected to draw
     * @param fromUpperRow   a boolean indicating whether the card is being drawn from the upper row of the game board
     */
    public void drawCard(ClientSession clientSession, int selectedCardId, boolean fromUpperRow) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to draw a game.");
        }
        if (!clientSession.isInGame()) {
            throw new IllegalStateException("Player is not currently in a game and cannot draw a card.");
        }

        model.drawCard(clientSession.getActiveGameId(), clientSession.getNickname(), selectedCardId, fromUpperRow);
    }

    /**
     * Allows a player to place a Totem on an OfferBox position.
     *
     * @param clientSession    the client session of the player
     * @param offerBoxPosition the 1-based position of the offer box
     */
    public void placing(ClientSession clientSession, int offerBoxPosition) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to place a totem");
        }
        if (!clientSession.isInGame()) {
            throw new IllegalStateException("Player is not currently in a game and cannot place a totem.");
        }

        model.placing(clientSession.getActiveGameId(), clientSession.getNickname(), offerBoxPosition);
    }


    /**
     * Retrieves a list of available games that players can join
     *
     */
    public List<GameInfoDTO> getAvailableGames(ClientSession clientSession) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to get available games");
        }
        return model.getAvailableGames();
    }

    /**
     * Handles the disconnection of a client session, ensuring that the player's state is
     * properly updated in the game model and that any necessary cleanup is performed.
     *
     * @param clientSession the client session that has been disconnected
     */
    public List<ClientSession> handleDisconnect(ClientSession clientSession) {
        List<ClientSession> playersInterested = new ArrayList<>();

        String nickname;
        try {
            nickname = clientSession.getNickname();
        } catch (Exception e) {
            System.err.println("(Game portal) Player disconnected");
            clientSession.disconnectSession();
            return playersInterested;
        }


        try {
            if (clientSession.isInGame()) {
                // If the player was in a game, we need to first leave the game and notify all the player about the game ended
                playersInterested.addAll(leaveGame(clientSession));
                //model.markPlayerDisconnected(gameId, nickname);
            }
            // Removes player from the logged players
            model.logout(nickname);
        } finally {
            // Ensure that the client session is properly disconnected and unregistered from the session registry
            SessionRegistry.getInstance().unregisterSession(clientSession);
            clientSession.disconnectSession();
        }

        return playersInterested;
    }

    /**
     * Refreshes the active game ID for a client session by comparing the current active game ID in the session with the actual active game ID from the model.
     *
     * @param clientSession the client session for which to refresh the active game ID
     */
    private void refreshActiveGameIdFromModel(ClientSession clientSession) {
        if (!clientSession.isLoggedIn()) {
            return;
        }

        Integer actualActiveGameId = model.getActiveGameIdForPlayer(clientSession.getNickname());
        Integer sessionActiveGameId = clientSession.getActiveGameId();

        if (actualActiveGameId == null) {
            if (sessionActiveGameId != null) {
                clientSession.clearActiveGameId();
            }
            return;
        }

        if (!actualActiveGameId.equals(sessionActiveGameId)) {
            if (sessionActiveGameId != null) {
                clientSession.clearActiveGameId();
            }
            clientSession.setActiveGameId(actualActiveGameId);
        }
    }

    /**
     * Checks for an existing client session associated with the given nickname and disconnects it if it is found to be stale
     *
     * @param nickname the nickname of the player for which to check for an existing session
     */
    private void disconnectStaleSessionIfPresent(String nickname) {
        ClientSession existingSession = SessionRegistry.getInstance().getSession(nickname);
        if (existingSession == null) {
            return;
        }
        try {
            if (existingSession.getEventHandler().getView().isReachable()) {
                return;
            }
        } catch (Exception e) {
            System.out.println("[INFO] Detected stale session for " + nickname + ", forcing disconnect");
        }
        handleDisconnect(existingSession);
    }

    /**
     * Retrieves the rankings of players based on their final scores, limited to a specified number of top players.
     *
     * @param clientSession the client session of the player requesting the rankings
     * @param numPlayers    the number of top players to include in the rankings
     * @return a list of LeadBoardDTO objects representing the rankings of players
     */
    public List<LeadBoardDTO> getRankings(ClientSession clientSession, int numPlayers) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to get rankings");
        }
        return model.getRanking(numPlayers);
    }

    public List<ClientSession> leaveLobby(ClientSession clientSession) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to leave a lobby");
        }

        List<ClientSession> playersInterested = new ArrayList<>();
        if (clientSession.isInGame()) {
            List<String> remainingPlayerNicknames = model.leaveLobby(clientSession.getNickname(), clientSession.getActiveGameId());
            for (String remainingPlayerNickname : remainingPlayerNicknames) {
                ClientSession playerSession = SessionRegistry.getInstance().getSession(remainingPlayerNickname);
                playerSession.clearActiveGameId();
                playersInterested.add(playerSession);
            }
            clientSession.clearActiveGameId();
        }

        return playersInterested;
    }

    /**
     * Handles the unexpected disconnection of a client session, ensuring that the player's state is properly
     * updated in the game model and that any necessary cleanup is performed.
     * @param clientSession the client session that has been unexpectedly disconnected
     */
    public void handleUnexpectedDisconnect(ClientSession clientSession) {
        if (clientSession == null || clientSession.isRequestedDisconnection()) {
            return;
        }

        String nickname = null;
        if (clientSession.isLoggedIn()) {
            try {
                nickname = clientSession.getNickname();
            } catch (Exception ignored) {
            }
        }

        List<ClientSession> playersToNotify = handleDisconnect(clientSession);

        if (nickname != null) {
            notifyGameClosed(
                    playersToNotify,
                    nickname + " has disconnected, the game is ended"
            );
        }
    }

    /**
     * Notifies a list of player sessions that a game has been closed, providing a reason for the closure and updating their available games list.
     * @param playersToNotify the list of client sessions to notify about the game closure
     * @param reason the reason for the game closure to be communicated to the players
     */
    public void notifyGameClosed(List<ClientSession> playersToNotify, String reason) {
        for (ClientSession playerSession : playersToNotify) {
            if (playerSession == null || !playerSession.isLoggedIn()) {
                continue;
            }

            try {
                playerSession.getEventHandler().getView().onGameCrashed(reason);
                playerSession.getEventHandler().getView().onAvailableGames(getAvailableGames(playerSession));
            } catch (Exception e) {
                System.err.println("[WARN] Could not notify player about closed game: " + e.getMessage());
            }
        }
    }

    /**
     * Iterates through all active client sessions and disconnects any sessions that are found to be unreachable
     */
    public void disconnectUnreachableSessions() {
        for (ClientSession session : SessionRegistry.getInstance().getSessionsSnapshot()) {
            if (session == null || !session.isLoggedIn() || session.isRequestedDisconnection()) {
                continue;
            }

            try {
                if (!session.getEventHandler().getView().isReachable()) {
                    handleUnexpectedDisconnect(session);
                }
            } catch (Exception e) {
                handleUnexpectedDisconnect(session);
            }
        }
    }


}
