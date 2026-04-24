package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.game.CreateGameMessage;
import it.polimi.ingsw.client.network.socket.messages.game.JoinGameMessage;
import it.polimi.ingsw.client.network.socket.messages.login.LoginRequestMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterRequestMessage;
import it.polimi.ingsw.server.controller.GameController;

public class ClientVisitor implements ClientMessageVisitor {

    private final GameController gameController;

    public ClientVisitor(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void visit(LoginRequestMessage message) {
        gameController.login(message.getUsername(), message.getPasswordHash());
    }

    @Override
    public void visit(RegisterRequestMessage message) {
        gameController.register(message.getUsername(), message.getPassword());
    }

    @Override
    public void visit(CreateGameMessage message) {
        gameController.createGame(message.getUsername());
    }

    @Override
    public void visit(JoinGameMessage message) {
        gameController.join(message.getGameId(), message.getPlayerNickname());
    }
}
