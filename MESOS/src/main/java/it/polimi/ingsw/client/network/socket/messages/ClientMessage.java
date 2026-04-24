package it.polimi.ingsw.client.network.socket.messages;

import it.polimi.ingsw.server.network.socket.messages.ClientVisitor;

public abstract class ClientMessage {
    public abstract void dispatch(ClientVisitor  clientVisitor);
}
