package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.core.*;

/**
 * Extra action -> Eventi + Modifiers ->
 */
public class EndRoundState implements IGameState {
    @Override
    public void nextState(Game game) {
        if (game.isLastRound()) {
            game.setLastRoundTriggered(true);
        }
    }
}
