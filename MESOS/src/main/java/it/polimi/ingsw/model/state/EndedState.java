package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.core.Game;

public class EndedState implements IGameState {
    @Override
    public void nextState(Game game) {
        // No next state, the game has ended.
    }
}
