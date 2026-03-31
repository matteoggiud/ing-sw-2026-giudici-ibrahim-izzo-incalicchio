package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.core.*;

public class PlacingTotemsState implements IGameState{
    @Override
    public void nextState(Game game) {
        game.setGameState(new ResolveActionsState());
    }
}
