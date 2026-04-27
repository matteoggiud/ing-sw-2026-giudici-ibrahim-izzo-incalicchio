package it.polimi.ingsw.client.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientMessageVisitor;

public class CreateGameMessage extends ClientMessage {

    private final int expectedNumOfPlayers;

    public CreateGameMessage(int expectedNumOfPlayers) {
        this.expectedNumOfPlayers = expectedNumOfPlayers;
    }

    public int getExpectedNumOfPlayers() {
        return expectedNumOfPlayers;
    }

    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
