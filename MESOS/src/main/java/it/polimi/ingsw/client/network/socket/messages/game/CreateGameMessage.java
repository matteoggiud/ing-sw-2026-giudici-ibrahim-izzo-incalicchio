package it.polimi.ingsw.client.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientVisitor;

public class CreateGameMessage extends ClientMessage {
    private final String username;

    public CreateGameMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void dispatch(ClientVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
