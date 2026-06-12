package it.polimi.ingsw.common.network.socket_messages.client;

import it.polimi.ingsw.common.network.socket_messages.visitors.ClientMessageVisitor;

import java.io.Serializable;

public abstract class ClientMessage implements Serializable {
    public abstract void dispatch(ClientMessageVisitor clientVisitor);
}
