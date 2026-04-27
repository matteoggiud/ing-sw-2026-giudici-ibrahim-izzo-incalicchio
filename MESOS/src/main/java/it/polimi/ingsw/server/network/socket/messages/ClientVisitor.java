package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.game.CreateGameMessage;
import it.polimi.ingsw.client.network.socket.messages.game.JoinGameMessage;
import it.polimi.ingsw.client.network.socket.messages.login.LoginSubmitMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetAvailableGamesMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterSubmitMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.network.socket.ClientSession;
import it.polimi.ingsw.server.network.socket.messages.login.LoginFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.AvailableGamesMessage;
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

    public ClientSession getClientSession() {
        return clientSession;
    }

    public GameController getGameController() {
        return gameController;
    }

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

    @Override
    public void visit(CreateGameMessage message) {
        gameController.createGame(clientSession);
    }

    @Override
    public void visit(JoinGameMessage message) {
        gameController.joinGame(clientSession, message.getGameId());
    }

    @Override
    public void visit(GetAvailableGamesMessage message) {
        List<GameInfoDTO> games = gameController.getAvailableGames(clientSession);
        clientSession.getVirtualView().sendMessage(new AvailableGamesMessage(games));
    }
}
