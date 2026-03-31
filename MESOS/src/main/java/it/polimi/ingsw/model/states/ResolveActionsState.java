package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.core.*;

public class ResolveActionsState implements IGameState{
    @Override
    public void nextState(Game game) {
        game.setGameState(new EndRoundState());
    }
}
