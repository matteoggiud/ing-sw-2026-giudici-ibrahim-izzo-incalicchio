package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.client.ServerMessageVisitor;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

import java.util.List;

public class GameStartedMessage extends ServerMessage {
    private final int gameId;
    private final List<String> playerNicknames;

    public GameStartedMessage(int gameId, List<String> playerNicknames) {
        this.gameId = gameId;
        this.playerNicknames = playerNicknames;
    }

    public int getGameId() {
        return gameId;
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
