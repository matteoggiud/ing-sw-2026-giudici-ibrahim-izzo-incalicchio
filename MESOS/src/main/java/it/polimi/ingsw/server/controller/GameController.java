package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.LeadBoardDTO;
import it.polimi.ingsw.server.network.ClientSession;
import it.polimi.ingsw.server.network.SessionRegistry;

import java.sql.SQLException;
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

        try {
            model.login(username, password);
            clientSession.setNickname(username);

            // Register the client session in the session registry to allow for proper session management and cleanup on disconnect
            SessionRegistry.getInstance().registerSession(clientSession);

        } catch (SQLException e) {
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

        try {
            model.register(username, password);

            // Automatically log in the player after successful registration
            clientSession.setNickname(username);
            // Register the client session in the session registry to allow for proper session management and cleanup on disconnect
            SessionRegistry.getInstance().registerSession(clientSession);
        } catch (SQLException e) {
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

        model.joinGame(gameId, clientSession.getNickname(), clientSession.getVirtualView());


        // update the client session to reflect the game they have joined
        clientSession.setActiveGameId(gameId);

        model.startGameIfReady(gameId);

    }

    /**
     * Allows a player to leave a game they have joined using the game's unique identifier and the player's username
     * It also disconnects the player's virtual view from the game they have left and updates the client session
     *
     * @param clientSession the client session of the player
     * @throws IllegalStateException if the player is not logged in or not currently in a game
     */
    public void leaveGame(ClientSession clientSession) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to leave a game.");
        }
        if (!clientSession.isInGame()) {
            throw new IllegalStateException("Player is not currently in a game and cannot leave it.");
        }

        // Update the client session to reflect that the player has left the game
        List<String> remainingPlayers = model.leaveGame(clientSession.getActiveGameId(), clientSession.getNickname(), clientSession.getVirtualView());
        for (String player : remainingPlayers) {
            SessionRegistry.getInstance().getSession(player).clearActiveGameId();
        }
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

        int gameId = model.createGame(clientSession.getNickname(), expectedPlayerCount, clientSession.getVirtualView());
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
    public void handleDisconnect(ClientSession clientSession) {
        if (!clientSession.isLoggedIn()) {
            return;
        }

        String nickname = clientSession.getNickname();

        if (clientSession.isInGame()) {
            int gameId = clientSession.getActiveGameId();

            try {
                leaveGame(clientSession);
            } catch (RuntimeException ignored) {
                // The observer may already have been removed.
            }

            model.markPlayerDisconnected(gameId, nickname);
            return;
        }

        try {
            model.logout(nickname);
            // Unregister the client session from the session registry to ensure proper cleanup and prevent memory leaks
            SessionRegistry.getInstance().unregisterSession(clientSession);
        } catch (RuntimeException ignored) {
            // The player may already have been removed.
        }

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
     * Retrieves the rankings of players based on their final scores, limited to a specified number of top players.
     * @param clientSession the client session of the player requesting the rankings
     * @param numPlayers the number of top players to include in the rankings
     * @return a list of LeadBoardDTO objects representing the rankings of players
     */
    public List<LeadBoardDTO> getRankings(ClientSession clientSession, int numPlayers) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to get rankings");
        }
        return model.getRanking(numPlayers);
    }

}
