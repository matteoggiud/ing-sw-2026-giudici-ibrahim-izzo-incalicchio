package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.game.CreateGameMessage;
import it.polimi.ingsw.client.network.socket.messages.game.JoinGameMessage;
import it.polimi.ingsw.client.network.socket.messages.login.LoginRequestMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterRequestMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.socket.ClientSession;

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
    public void visit(LoginRequestMessage message) {
        gameController.login(clientSession, message.getUsername(), message.getPasswordHash());
    }

    @Override
    public void visit(RegisterRequestMessage message) {
        gameController.register(clientSession, message.getUsername(), message.getPassword());
    }

    @Override
    public void visit(CreateGameMessage message) {
        gameController.createGame(clientSession);
    }

    @Override
    public void visit(JoinGameMessage message) {
        gameController.joinGame(clientSession, message.getGameId());
    }
}
