package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.visitors.ServerMessageVisitor;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

public class GameCrashedMessage extends ServerMessage {


    public GameCrashedMessage(String reason) {
        super(reason);
    }

    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }
}
