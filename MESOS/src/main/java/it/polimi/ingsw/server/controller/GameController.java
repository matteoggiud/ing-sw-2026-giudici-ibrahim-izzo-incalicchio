package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.network.socket.ClientSession;

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
     * Allows a player to log in to the game using their username and password
     *
     * @param clientSession the client session of the player
     * @param username      the username of the player attempting to log in
     * @param password      the password of the player attempting to log in
     */
    public void login(ClientSession clientSession, String username, String password) throws RuntimeException {
        try {
            model.login(username, password);
            clientSession.setNickname(username);

            Integer activeGameId = model.getActiveGameIdForPlayer(username);
            if (activeGameId != null) {
                clientSession.setActiveGameId(activeGameId);
                model.addObserver(activeGameId, clientSession.getVirtualView());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }


    /**
     * Allows a new player to register an account using a username and password
     *
     * @param clientSession the client session of the player
     * @param username      the desired username for the new player account
     * @param password      the desired password for the new player account
     */
    public void register(ClientSession clientSession, String username, String password) throws RuntimeException {
        try {
            model.register(username, password);

            // Automatically log in the player after successful registration
            clientSession.setNickname(username);
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
        if (clientSession.isInGame()) {
            throw new IllegalStateException("Player is already in a game and cannot join another.");
        }

        model.joinGame(gameId, clientSession.getNickname());

        // connect the client virtual view to the game joined observer
        model.addObserver(gameId, clientSession.getVirtualView());

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
            throw new IllegalStateException("Player must be logged in to join a game.");
        }
        if (!clientSession.isInGame()) {
            throw new IllegalStateException("Player is not currently in a game and cannot leave.");
        }

        model.leaveGame(clientSession.getActiveGameId(), clientSession.getNickname());

        // disconnect the client virtual view from the game left observer
        model.removeObserver(clientSession.getActiveGameId(), clientSession.getVirtualView());

        // update the client session to reflect that they have left the game
        clientSession.clearActiveGameId();
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

        model.logout(clientSession.getNickname());
    }

    /**
     * Allows a player to create a new game, which they will host
     * @param clientSession the client session of the player
     */
    public void createGame(ClientSession clientSession) {
        createGame(clientSession, 2);
    }

    /**
     * Allows a player to create a new game with a specified expected player count, which they will host
     * @param clientSession the client session of the player
     * @param expectedPlayerCount the expected number of players for the game being created
     */
    public void createGame(ClientSession clientSession, int expectedPlayerCount) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to create a game.");
        }
        if (clientSession.isInGame()) {
            throw new IllegalStateException("Player is currently in a game and cannot create another.");
        }

        int gameId = model.createGame(clientSession.getNickname(), expectedPlayerCount);
        clientSession.setActiveGameId(gameId);

        model.addObserver(gameId, clientSession.getVirtualView());
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
     * Allows a player to place a Totem on the OfferBox
     *
     * @param clientSession the client session of the player
     * @param offerBoxId the identifier of the offer box on which the player is placing their totem
     */
    public void placing(ClientSession clientSession, String offerBoxId) {
        if (!clientSession.isLoggedIn()) {
            throw new IllegalStateException("Player must be logged in to place a totem");
        }
        if (!clientSession.isInGame()) {
            throw new IllegalStateException("Player is not currently in a game and cannot place a totem.");
        }

        model.placing(clientSession.getActiveGameId(), clientSession.getNickname(), offerBoxId);
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
            clientSession.disconnectSession();
            return;
        }

        String nickname = clientSession.getNickname();

        if (clientSession.isInGame()) {
            int gameId = clientSession.getActiveGameId();

            try {
                model.removeObserver(gameId, clientSession.getVirtualView());
            } catch (RuntimeException ignored) {
                // The observer may already have been removed.
            }

            model.markPlayerDisconnected(gameId, nickname);
            clientSession.clearActiveGameId();
            clientSession.disconnectSession();
            return;
        }

        try {
            model.logout(nickname);
        } catch (RuntimeException ignored) {
            // The player may already have been removed.
        }

        clientSession.disconnectSession();
    }

}
