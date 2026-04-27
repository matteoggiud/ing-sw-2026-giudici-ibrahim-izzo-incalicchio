package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.game.CreateGameMessage;
import it.polimi.ingsw.client.network.socket.messages.game.JoinGameMessage;
import it.polimi.ingsw.client.network.socket.messages.login.*;
import it.polimi.ingsw.client.network.socket.messages.menu.GetAvailableGamesMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterSubmitMessage;

public interface ClientMessageVisitor {
    void visit(LoginSubmitMessage message);
    void visit(RegisterSubmitMessage message);
    void visit(CreateGameMessage message);
    void visit(JoinGameMessage message);
    void visit(GetAvailableGamesMessage message);
}
