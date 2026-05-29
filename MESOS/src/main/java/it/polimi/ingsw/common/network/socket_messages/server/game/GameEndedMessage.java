package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;
import it.polimi.ingsw.common.network.socket_messages.client.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.LeadBoardDTO;
import java.util.List;

import java.util.Map;

public class GameEndedMessage extends ServerMessage {
    private final int gameId;
    private final Map<String, Integer> finalRanking;
    private final int numPlayers;
    private final List<LeadBoardDTO> historicalLeaderboard;

    public GameEndedMessage(int gameId, Map<String, Integer> finalRanking, int numPlayers, List<LeadBoardDTO> historicalLeaderboard) {
        this.gameId = gameId;
        this.finalRanking = finalRanking;
        this.numPlayers = numPlayers;
        this.historicalLeaderboard = historicalLeaderboard;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public List<LeadBoardDTO> getHistoricalLeaderboard() {
        return historicalLeaderboard;
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
