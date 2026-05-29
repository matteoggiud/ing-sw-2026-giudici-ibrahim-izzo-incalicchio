package it.polimi.ingsw.common.network.socket_messages.client.menu;

import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.server.ClientMessageVisitor;

public class GetRankingsMessage extends ClientMessage {
    private final int numPlayers;

    public GetRankingsMessage(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    @Override
    public void dispatch(ClientMessageVisitor clientVisitor){
        clientVisitor.visit(this);
    }
}
