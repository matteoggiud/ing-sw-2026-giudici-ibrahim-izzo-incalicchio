package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.game.CreateGameMessage;
import it.polimi.ingsw.client.network.socket.messages.login.*;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterRequestMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginResponseMessage;

public interface ClientMessageVisitor {
    void visit(LoginRequestMessage message);
    void visit(RegisterRequestMessage message);
    void visit(CreateGameMessage message);
}
