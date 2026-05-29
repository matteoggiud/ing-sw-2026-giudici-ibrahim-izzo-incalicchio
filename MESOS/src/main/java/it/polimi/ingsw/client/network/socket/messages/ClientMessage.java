package it.polimi.ingsw.client.network.socket.messages;

import it.polimi.ingsw.common.network.socket_messages.server.ClientMessageVisitor;

import java.io.Serializable;

public abstract class ClientMessage implements Serializable {
    public abstract void dispatch(ClientMessageVisitor clientVisitor);
}
