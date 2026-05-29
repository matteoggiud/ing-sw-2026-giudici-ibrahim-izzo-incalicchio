package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

import java.util.List;

public class UpdatePlayersMessage extends ServerMessage {
    private final List<String> playerNicknames;

    public UpdatePlayersMessage(List<String> playerNicknames) {
        this.playerNicknames = playerNicknames;
    }

    public List<String> getPlayerNicknames() {
        return playerNicknames;
    }

    /**
     * Accepts a visitor that will handle the message
     *
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }
}
