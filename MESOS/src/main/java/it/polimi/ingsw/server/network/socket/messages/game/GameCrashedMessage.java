package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

public class GameCrashedMessage extends ServerMessage {


    public GameCrashedMessage(String reason) {
        super(reason);
    }

    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }
}
