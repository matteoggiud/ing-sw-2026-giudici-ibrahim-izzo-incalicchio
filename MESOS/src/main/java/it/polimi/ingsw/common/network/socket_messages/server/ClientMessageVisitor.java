package it.polimi.ingsw.common.network.socket_messages.server;

import it.polimi.ingsw.common.network.socket_messages.client.game.*;
import it.polimi.ingsw.common.network.socket_messages.client.menu.*;
import it.polimi.ingsw.common.network.socket_messages.client.login.LoginSubmitMessage;
import it.polimi.ingsw.common.network.socket_messages.client.registration.RegisterSubmitMessage;

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
    void visit(LeftLobbyMessage message);
}
