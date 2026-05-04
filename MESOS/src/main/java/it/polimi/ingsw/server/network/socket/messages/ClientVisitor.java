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
            clientSession.getVirtualView().sendMessage(new LoginSuccessMessage("Login successful, welcome " + message.getUsername(), message.getUsername()));

            // After a successful login, send the list of available games to the client
            List<GameInfoDTO> games = gameController.getAvailableGames(clientSession);
            clientSession.getVirtualView().sendMessage(new AvailableGamesMessage(games));
        } catch (RuntimeException e) {
            // If login fails, send a LoginFailedMessage to the client with the error message
            clientSession.getVirtualView().sendMessage(new LoginFailedMessage("Login failed", message.getUsername()));
        } catch (Exception e) {
            // If login fails due to any other exception, send a GenericErrorMessage to the client with the error message
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Login failed"));
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
            clientSession.getVirtualView().sendMessage(new RegisterSuccessMessage("Registration successful, welcome " + message.getUsername(), message.getUsername()));

            // After a successful login, send the list of available games to the client
            List<GameInfoDTO> games = gameController.getAvailableGames(clientSession);
            clientSession.getVirtualView().sendMessage(new AvailableGamesMessage(games));
        } catch (RuntimeException e) {
            // If registration fails, send a LoginFailedMessage to the client with the error message
            clientSession.getVirtualView().sendMessage(new RegisterFailedMessage("Registration failed", message.getUsername()));
        } catch (Exception e) {
            // If registration fails due to any other exception, send a GenericErrorMessage to the client with the error message
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Registration failed"));
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
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Failed to create game: " + e.getMessage()));
        } catch (Exception e) {
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Failed to create game"));
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
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Failed to join game: " + e.getMessage()));
        } catch (Exception e) {
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Failed to join game"));
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
            clientSession.getVirtualView().sendMessage(new AvailableGamesMessage(games));
        } catch (RuntimeException e) {
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Failed to retrieve available games: " + e.getMessage()));
        } catch (Exception e) {
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Failed to retrieve available games"));
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
            clientSession.getVirtualView().sendMessage(new DisconnectSuccessMessage("Disconnected from server, see you " + nickname));
        } catch (Exception e) {
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Failed to disconnect"));
        } finally {
            gameController.handleDisconnect(clientSession);
            clientSession.disconnectSession();
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
            gameController.leaveGame(clientSession);
            clientSession.getVirtualView().sendMessage(new LeftGameSuccessMessage("Left game successfully"));

            clientSession.getVirtualView().sendMessage(new AvailableGamesMessage(gameController.getAvailableGames(clientSession)));
        } catch (RuntimeException e) {
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Failed to leave game: " + e.getMessage()));
        } catch (Exception e) {
            clientSession.getVirtualView().sendMessage(new GenericErrorMessage("Failed to leave game"));
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
            clientSession.getVirtualView().sendMessage(
                    new GenericErrorMessage("Failed to place totem: " + e.getMessage())
            );
        } catch (Exception e) {
            clientSession.getVirtualView().sendMessage(
                    new GenericErrorMessage("Failed to place totem")
            );
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
            clientSession.getVirtualView().sendMessage(
                    new GenericErrorMessage("Failed to draw card: " + e.getMessage())
            );
        } catch (Exception e) {
            clientSession.getVirtualView().sendMessage(
                    new GenericErrorMessage("Failed to draw card")
            );
        }
    }

    /**
     * Handles the client's request to draw an extra card from the game.
     * @param message the message containing the details of the extra draw card action
     */
    @Override
    public void visit(ExtraDrawMessage message) {
        try {
            gameController.drawCard(clientSession, message.getSelectedCardId(), true);
        } catch (RuntimeException e) {
            clientSession.getVirtualView().sendMessage(
                    new GenericErrorMessage("Failed to draw extra card: " + e.getMessage())
            );
        } catch (Exception e) {
            clientSession.getVirtualView().sendMessage(
                    new GenericErrorMessage("Failed to draw extra card")
            );
        }
    }

    @Override
    public void visit(GetRankingsMessage message) {
        try {
            List<LeadBoardDTO> rankings = gameController.getRankings(clientSession, message.getNumPlayers());
            clientSession.getVirtualView().sendMessage(new RankingsMessage(rankings));
        } catch (RuntimeException e) {
            clientSession.getVirtualView().sendMessage(
                    new GenericErrorMessage("Failed to get rankings: " + e.getMessage())
            );
        } catch (Exception e) {
            clientSession.getVirtualView().sendMessage(
                    new GenericErrorMessage("Failed to get rankings")
            );
        }
    }
}
