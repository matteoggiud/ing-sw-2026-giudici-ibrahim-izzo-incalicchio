package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class EndedState implements IGameState {
    private final Map<Player, Integer> finalRanking;

    /**
     * Constructs a new EndedState with the given final ranking of players.
     * @param finalRanking the final ranking of players, must not be null
     */
    public EndedState(Map<Player, Integer> finalRanking) {
        if (finalRanking == null) {
            throw new IllegalArgumentException("Final ranking cannot be null.");
        }

        this.finalRanking = new LinkedHashMap<>(finalRanking);
    }

    /**
     * Since this is a terminal state, there are no next states to transition to. This method does nothing.
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        // Terminal state: no next state.
    }

    /**
     * get the final ranking of the game
     * @param game the current game context
     * @return the final ranking of players, never null
     */
    @Override
    public Map<Player, Integer> getFinalRanking(Game game) {
        return new LinkedHashMap<>(finalRanking);
    }


    /**
     * Since this is a terminal state, the game is considered ended. This method always returns true.
     * @return true, indicating that the game has ended
     */
    @Override
    public boolean isEnded() {
        return true;
    }


    /**
     * Returns the name of this state for persistence purposes.
     * @return the name of this state, "EndedState"
     */
    @Override
    public String getPersistenceName() {
        return "EndedState";
    }
}
