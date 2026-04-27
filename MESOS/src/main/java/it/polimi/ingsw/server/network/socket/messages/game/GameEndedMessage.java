package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.server.network.socket.messages.ServerMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;

import java.util.Map;

public class GameEndedMessage extends ServerMessage {
    private final int gameId;
    private final Map<String, Integer> finalRanking;

    public GameEndedMessage(int gameId, Map<String, Integer> finalRanking) {
        this.gameId = gameId;
        this.finalRanking = finalRanking;
    }

    public int getGameId() {
        return gameId;
    }
    public Map<String, Integer> getFinalRanking() {
        return finalRanking;
    }

    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }
}
