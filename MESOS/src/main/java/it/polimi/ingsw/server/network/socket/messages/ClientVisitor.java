package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.game.*;
import it.polimi.ingsw.client.network.socket.messages.login.LoginSubmitMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.DisconnectMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetAvailableGamesMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetRankingsMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.LeftGameMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterSubmitMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.LeadBoardDTO;
import it.polimi.ingsw.server.network.ClientSession;
import it.polimi.ingsw.server.network.GenericVirtualView;
import it.polimi.ingsw.server.network.SessionRegistry;
import it.polimi.ingsw.server.network.socket.messages.login.LoginFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.AvailableGamesMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.DisconnectSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.LeftGameSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.RankingsMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterSuccessMessage;

import java.util.List;

public class ClientVisitor implements ClientMessageVisitor {

    private final GameController gameController;
    private final ClientSession clientSession;

    public ClientVisitor(GameController gameController, ClientSession clientSession) {
        this.gameController = gameController;
        this.clientSession = clientSession;
    }

    /**
     * Returns the client session associated with this visitor.
     *
     * @return the ClientSession instance
     */
    public ClientSession getClientSession() {
        return clientSession;
    }

    /**
     * Returns the game controller associated with this visitor.
     *
     * @return the GameController instance
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Handles the client's request to log in.
     *
     * @param message the message containing the login credentials
     */
    @Override
    public void visit(LoginSubmitMessage message) {
        try {
            // Attempt to log in the user with the provided username and passwordHash hash
            gameController.login(clientSession, message.getUsername(), message.getPasswordHash());

            // If login is successful, send a LoginSuccessMessage to the client
            clientSession.getEventHandler().getView().onLoginSuccess(message.getUsername(), "Login successful, welcome " + message.getUsername());

            // After a successful login, send the list of available games to the client
            List<GameInfoDTO> games = gameController.getAvailableGames(clientSession);
            clientSession.getEventHandler().getView().onAvailableGames(games);
        } catch (RuntimeException e) {
            // If login fails, send a LoginFailedMessage to the client with the error message
            clientSession.getEventHandler().getView().onLoginFailed("Login failed: " + e.getMessage(), message.getUsername());
        } catch (Exception e) {
            // If login fails due to any other exception, send a GenericErrorMessage to the client with the error message
            clientSession.getEventHandler().getView().onGenericError("Login failed");
        }
    }

    /**
     * Handles the client's request to register a new account.
     *
     * @param message the message containing the registration credentials
     */
    @Override
    public void visit(RegisterSubmitMessage message) {
        try {
            // Attempt to register the user with the provided username and passwordHash, if successful, log them in immediately
            gameController.register(clientSession, message.getUsername(), message.getPassword());

            // If registration is successful, send a RegisterSuccessMessage to the client
            clientSession.getEventHandler().getView().onRegisterSuccess(message.getUsername(), "Registration successful, welcome " + message.getUsername());

            // After a successful login, send the list of available games to the client
            List<GameInfoDTO> games = gameController.getAvailableGames(clientSession);
            clientSession.getEventHandler().getView().onAvailableGames(games);
        } catch (RuntimeException e) {
            // If registration fails, send a LoginFailedMessage to the client with the error message
            clientSession.getEventHandler().getView().onRegisterFailed("Registration failed: " + e.getMessage(), message.getUsername());
        } catch (Exception e) {
            // If registration fails due to any other exception, send a GenericErrorMessage to the client with the error message
            clientSession.getEventHandler().getView().onGenericError("Registration failed");
        }
    }

    /**
     * Handles the client's request to create a new game.
     *
     * @param message the message containing the game creation details
     */
    @Override
    public void visit(CreateGameMessage message) {
        try {
            gameController.createGame(clientSession, message.getExpectedNumOfPlayers());
        } catch (RuntimeException e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to create game: " + e.getMessage());
        } catch (Exception e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to create game");
        }
    }

    /**
     * Handles the client's request to join an existing game.
     *
     * @param message the message containing the game ID to join
     */
    @Override
    public void visit(JoinGameMessage message) {
        try {
            gameController.joinGame(clientSession, message.getGameId());
        } catch (RuntimeException e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to join game: " + e.getMessage());
        } catch (Exception e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to join game");
        }
    }

    /**
     * Handles the client's request to retrieve the list of available games.
     *
     * @param message the message requesting available games
     */
    @Override
    public void visit(GetAvailableGamesMessage message) {
        try {
            List<GameInfoDTO> games = gameController.getAvailableGames(clientSession);
            clientSession.getEventHandler().getView().onAvailableGames(games);
        } catch (RuntimeException e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to retrieve available games: " + e.getMessage());
        } catch (Exception e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to retrieve available games");
        }
    }

    /**
     * Handles the client's request to disconnect from the server.
     *
     * @param message the disconnect message from the client
     */
    @Override
    public void visit(DisconnectMessage message) {
        try {
            String nickname = clientSession.isLoggedIn() ? clientSession.getNickname() : "";
            List<ClientSession> playersToNotifyGameLeft = gameController.handleDisconnect(clientSession);

            clientSession.getEventHandler().getView().onDisconnectionSuccess("Goodbye " + nickname);
            for (ClientSession playerToNotify : playersToNotifyGameLeft) {
                playerToNotify.getEventHandler().getView().onGameCrashed(nickname + " has disconnected, the game is ended");
                playerToNotify.getEventHandler().getView().onAvailableGames(gameController.getAvailableGames(playerToNotify));
            }
        } catch (Exception e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to disconnect");
        }
    }

    /**
     * Handles the client's request to leave an active game.
     *
     * @param message the message indicating the client wants to leave the game
     */
    @Override
    public void visit(LeftGameMessage message) {
        try {
            int gameId = clientSession.getActiveGameId();
            List<ClientSession> playersInterested = gameController.leaveGame(clientSession);

            // Notify the client that they have successfully left the game and update their available games list
            clientSession.getEventHandler().getView().onLeftGameSuccess("You have left the game");
            clientSession.getEventHandler().getView().onAvailableGames(gameController.getAvailableGames(clientSession));


            // Notify all other players interested in the game that a player has left and the game will be closed
            for (ClientSession player : playersInterested) {
                player.getEventHandler().getView().onGameCrashed(clientSession.getNickname() + " has left the game");
                player.getEventHandler().getView().onAvailableGames(gameController.getAvailableGames(player));
            }

        } catch (RuntimeException e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to leave game: " + e.getMessage());
        } catch (Exception e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to leave game");
        }
    }

    /**
     * Handles the client's request to place a totem on the game board.
     *
     * @param message the message containing the details of the place totem action
     */
    @Override
    public void visit(PlaceTotemMessage message) {
        try {
            gameController.placing(clientSession, message.getOfferBoxPosition());
        } catch (RuntimeException e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to place totem: " + e.getMessage());
        } catch (Exception e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to place totem");
        }
    }

    /**
     * Handles the client's request to draw a card from the game.
     *
     * @param message the message containing the details of the draw card action
     */
    @Override
    public void visit(DrawCardMessage message) {
        try {
            gameController.drawCard(clientSession, message.getSelectedCardId(), message.isFromUpperRow());
        } catch (RuntimeException e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to draw card: " + e.getMessage());
        } catch (Exception e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to draw card");
        }
    }

    /**
     * Handles the client's request to draw an extra card from the game.
     *
     * @param message the message containing the details of the extra draw card action
     */
    @Override
    public void visit(ExtraDrawMessage message) {
        try {
            gameController.drawCard(clientSession, message.getSelectedCardId(), true);
        } catch (RuntimeException e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to draw card: " + e.getMessage());
        } catch (Exception e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to draw card");
        }
    }

    @Override
    public void visit(GetRankingsMessage message) {
        try {
            List<LeadBoardDTO> rankings = gameController.getRankings(clientSession, message.getNumPlayers());
            clientSession.getEventHandler().getView().onRankingsReceived(rankings);
        } catch (RuntimeException e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to get rankings: " + e.getMessage());
        } catch (Exception e) {
            clientSession.getEventHandler().getView().onGenericError("Failed to get rankings");
        }
    }
}
