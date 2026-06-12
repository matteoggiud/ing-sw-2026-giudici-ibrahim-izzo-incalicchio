package it.polimi.ingsw.common.network.socket_messages.client.game;

import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.visitors.ClientMessageVisitor;

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
