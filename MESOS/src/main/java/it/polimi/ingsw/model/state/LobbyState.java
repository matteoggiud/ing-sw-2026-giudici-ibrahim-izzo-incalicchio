package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.core.Game;

public class LobbyState implements IGameState{
    @Override
    public void nextState(Game game) {
        game.setGameState(new SetupState());
    }
}
