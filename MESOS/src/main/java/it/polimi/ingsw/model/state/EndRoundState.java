package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.core.*;

public class EndRoundState implements IGameState{
    @Override
    public void nextState(Game game) {
        if(game.isLastRound()){
            game.setGameState(new FinalRoundState());
        } else {
            game.setGameState(new PlacingTotemsState());
        }
    }
}
