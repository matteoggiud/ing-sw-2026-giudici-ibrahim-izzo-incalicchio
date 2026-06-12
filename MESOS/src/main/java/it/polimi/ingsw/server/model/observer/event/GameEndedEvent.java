package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.common.network.dto.LeadBoardDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Event emitted when the game reaches the final scoring phase
 * and the final ranking becomes available.
 */
public class GameEndedEvent implements IGameEvent {

    private final int gameId;
    private final Map<String, Integer> finalRanking;
    private final int numPlayers;
    private final List<LeadBoardDTO> historicalLeaderboard;


    /**
     * Constructs a new game ended event.
     *
     * @param gameId       the unique identifier of the game
     * @param finalRanking the final ranking using player nicknames as keys
     */
    public GameEndedEvent(int gameId, Map<String, Integer> finalRanking, int numPlayers, List<LeadBoardDTO> historicalLeaderboard) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (finalRanking == null) {
            throw new IllegalArgumentException("Final ranking cannot be null.");
        }

        this.gameId = gameId;
        this.finalRanking = new LinkedHashMap<>(finalRanking);
        this.numPlayers = numPlayers;
        this.historicalLeaderboard = historicalLeaderboard;
    }

    /**
     * Returns the unique identifier of the game.
     *
     * @return the game id
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Returns the final ranking using player nicknames as keys.
     *
     * @return the final ranking
     */
    public Map<String, Integer> getFinalRanking() {
        return new LinkedHashMap<>(finalRanking);
    }

    /**
     * Returns the number of players in the game.
     * @return the number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Returns the historical leaderboard containing the top players and their scores.
     * @return the historical leaderboard
     */
    public List<LeadBoardDTO> getHistoricalLeaderboard() {
        return historicalLeaderboard;
    }

    /**
     * Dispatches this event to the observer.
     *
     * @param observer the observer to notify
     */
    @Override
    public void dispatch(IGameObserver observer) {
        observer.update(this);
    }

}
