package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.login.*;

public interface ClientMessageVisitor {
    void visit(LoginRequestMessage message);
}
