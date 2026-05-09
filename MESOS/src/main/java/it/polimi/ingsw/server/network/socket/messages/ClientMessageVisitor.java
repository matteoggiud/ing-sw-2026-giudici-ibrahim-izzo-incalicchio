package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.game.*;
import it.polimi.ingsw.client.network.socket.messages.login.*;
import it.polimi.ingsw.client.network.socket.messages.menu.DisconnectMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetAvailableGamesMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetRankingsMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.LeftGameMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterSubmitMessage;

public interface ClientMessageVisitor {
    void visit(LoginSubmitMessage message);
    void visit(RegisterSubmitMessage message);
    void visit(CreateGameMessage message);
    void visit(JoinGameMessage message);
    void visit(GetAvailableGamesMessage message);
    void visit(DisconnectMessage message);
    void visit (LeftGameMessage message);
    void visit(PlaceTotemMessage message);
    void visit(DrawCardMessage message);
    void visit(ExtraDrawMessage message);
    void visit(GetRankingsMessage message);
}
