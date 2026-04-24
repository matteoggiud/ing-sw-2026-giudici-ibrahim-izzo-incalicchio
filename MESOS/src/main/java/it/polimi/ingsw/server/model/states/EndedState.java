package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.core.Game;

public class EndedState implements IGameState {
    @Override
    public void nextState(Game game) {
        // No next state, the game has ended.
    }
}
