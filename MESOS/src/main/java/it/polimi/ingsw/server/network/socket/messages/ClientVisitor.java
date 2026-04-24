package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.login.LoginRequestMessage;
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
}
