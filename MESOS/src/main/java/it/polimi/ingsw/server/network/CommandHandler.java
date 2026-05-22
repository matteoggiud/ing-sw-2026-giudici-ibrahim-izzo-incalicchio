package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.LeadBoardDTO;

import java.util.List;

public class CommandHandler {

    private final GameController gameController;
    private final ClientSession clientSession;

    public CommandHandler(GameController gameController, ClientSession clientSession) {
        this.gameController = gameController;
        this.clientSession = clientSession;
    }

    /**
     * Returns the client session associated with this visitor.
     *
     * @return
     */
    public ClientSession getClientSession() {
        return clientSession;
    }

    /**
     * Returns the game controller associated with this visitor.
     *
     * @return
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Handles the client's request to log in to the server.
     *
     * @param username
     * @param password
     */
    public void handleLogin(String username, String password) {
        try {
            // Attempt to log in the user with the provided username and passwordHash hash
            gameController.login(clientSession, username, password);

            // If login is successful, send a LoginSuccessMessage to the client
            clientSession.getEventHandler().getView().onLoginSuccess(username, "Login successful, welcome " + username);

            if (!clientSession.isInGame()) {
                // After a successful login, if not reconnected to a game, send the list of available games
                List<GameInfoDTO> games = gameController.getAvailableGames(clientSession);
                clientSession.getEventHandler().getView().onAvailableGames(games);
            }
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleLogin RuntimeException): " + e.getMessage());
            // If login fails, send a LoginFailedMessage to the client with the error message
            clientSession.getEventHandler().getView().onLoginFailed("Login failed: " + e.getMessage(), username);
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleLogin Exception): " + e.getMessage());
            // If login fails due to any other exception, send a GenericErrorMessage to the client with the error message
            clientSession.getEventHandler().getView().onGenericError("Login failed");
        }
    }

    /**
     * Handles the client's request to register a new account on the server.
     *
     * @param username
     * @param password
     */
    public void handleRegistration(String username, String password) {
        try {
            // Attempt to register the user with the provided username and passwordHash, if successful, log them in immediately
            gameController.register(clientSession, username, password);

            // If registration is successful, send a RegisterSuccessMessage to the client
            clientSession.getEventHandler().getView().onRegisterSuccess(username, "Registration successful, welcome " + username);

            if (!clientSession.isInGame()) {
                // After a successful login, if not reconnected to a game, send the list of available games
                List<GameInfoDTO> games = gameController.getAvailableGames(clientSession);
                clientSession.getEventHandler().getView().onAvailableGames(games);
            }
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleRegistration RuntimeException): " + e.getMessage());
            // If registration fails, send a LoginFailedMessage to the client with the error message
            clientSession.getEventHandler().getView().onRegisterFailed("Registration failed: " + e.getMessage(), username);
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleRegistration Exception): " + e.getMessage());
            // If registration fails due to any other exception, send a GenericErrorMessage to the client with the error message
            clientSession.getEventHandler().getView().onGenericError("Registration failed");
        }
    }

    /**
     * Handles the client's request to create a new game on the server.
     *
     * @param expectedNumOfPlayers
     */
    public void handleGameCreation(int expectedNumOfPlayers) {
        try {
            gameController.createGame(clientSession, expectedNumOfPlayers);
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleGameCreation RuntimeException): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to create game: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleGameCreation Exception): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to create game");
        }
    }

    /**
     * Handles the client's request to join an existing game on the server.
     *
     * @param gameId
     */
    public void handleJoinGame(int gameId) {
        try {
            gameController.joinGame(clientSession, gameId);
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleJoinGame RuntimeException): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to join game: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleJoinGame Exception): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to join game");
        }
    }

    /**
     * Handles the client's request to retrieve the list of available games on the server.
     */
    public void handleGetAvailableGames() {
        try {
            List<GameInfoDTO> games = gameController.getAvailableGames(clientSession);
            clientSession.getEventHandler().getView().onAvailableGames(games);
            //clientSession.clearActiveGameId();
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleGetAvailableGames RuntimeException): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to retrieve available games: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleGetAvailableGames Exception): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to retrieve available games");
        }
    }

    /**
     * Handles the client's request to disconnect from the server, ensuring that any necessary
     * cleanup is performed and that other players are notified if the disconnection affects an active game.
     */
    public void handleDisconnection() {
        try {
            String nickname = clientSession.isLoggedIn() ? clientSession.getNickname() : "";
            List<ClientSession> playersToNotifyGameLeft = gameController.handleDisconnect(clientSession);

            gameController.notifyGameClosed(
                    playersToNotifyGameLeft,
                    nickname + " has disconnected, the game is ended"
            );

            //clientSession.getEventHandler().getView().onDisconnectionSuccess("Goodbye " + nickname);
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleDisconnection Exception): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to disconnect");
        }
    }

    /**
     * Handles the client's request to leave the current game, ensuring that any necessary cleanup is performed
     */
    public void handleLeftGame() {
        try {
            int gameId = clientSession.getActiveGameId();
            List<ClientSession> playersInterested = gameController.leaveGame(clientSession);

            // Notify the client that they have successfully left the game and update their available games list
            clientSession.getEventHandler().getView().onLeftGameSuccess("You have left the game");
            clientSession.getEventHandler().getView().onAvailableGames(gameController.getAvailableGames(clientSession));


            // Notify all other players interested in the game that a player has left and the game will be closed
            gameController.notifyGameClosed(
                    playersInterested,
                    clientSession.getNickname() + " has left the game, the game is ended"
            );

        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleLeftGame RuntimeException): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to leave game: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleLeftGame Exception): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to leave game");
        }
    }


    /**
     * Handles the client's request to place a totem on the game board, ensuring that the action is valid and updating the game state accordingly.
     *
     * @param offerBoxPosition
     */
    public void handlePlaceTotem(int offerBoxPosition) {
        try {
            gameController.placing(clientSession, offerBoxPosition);
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handlePlaceTotem RuntimeException): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to place totem: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handlePlaceTotem Exception): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to place totem");
        }
    }

    /**
     * Handles the client's request to draw a card from the game, ensuring that the action is valid and updating the game state
     *
     * @param cardId
     * @param isFromUpperRow
     */
    public void handleDrawCard(int cardId, boolean isFromUpperRow) {
        try {
            gameController.drawCard(clientSession, cardId, isFromUpperRow);
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleDrawCard RuntimeException): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to draw card: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleDrawCard Exception): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to draw card");
        }
    }

    /**
     * Handles the client's request to draw an extra card from the game, ensuring that the action is valid and updating the game state accordingly.
     *
     * @param cardId
     */
    public void handleExtraDraw(int cardId) {
        try {
            gameController.drawCard(clientSession, cardId, true);
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleExtraDraw RuntimeException): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to draw card: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleExtraDraw Exception): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to draw card");
        }
    }

    /**
     * Handles the client's request to retrieve the current player rankings for a game, ensuring that the information is accurate and up-to-date.
     *
     * @param numPlayers
     */
    public void handleRankings(int numPlayers) {
        try {
            List<LeadBoardDTO> rankings = gameController.getRankings(clientSession, numPlayers);
            clientSession.getEventHandler().getView().onRankingsReceived(rankings);
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleRankings RuntimeException): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to get rankings: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleRankings Exception): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to get rankings");
        }
    }

    public void handleLeftLobby() {
        try {
            List<ClientSession> playersInterested = gameController.leaveLobby(clientSession);

            for (ClientSession playerToNotify : playersInterested) {
                playerToNotify.getEventHandler().getView().onAvailableGames(gameController.getAvailableGames(playerToNotify));
            }
            clientSession.getEventHandler().getView().onAvailableGames(gameController.getAvailableGames(clientSession));
            clientSession.getEventHandler().getView().onLeftGameSuccess("You have left the lobby");
        } catch (RuntimeException e) {
            System.err.println("[ERROR] Exception caught in CommandHandler (handleLeftLobby RuntimeException): " + e.getMessage());
            clientSession.getEventHandler().getView().onGenericError("Failed to leave lobby: " + e.getMessage());
        }
    }
}
