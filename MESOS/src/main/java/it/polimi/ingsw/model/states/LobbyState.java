package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;

public class LobbyState implements IGameState {
    @Override
    public void nextState(Game game) {
        game.setGameState(new SetupState());
    }

    @Override
    public void addPlayer(Game game, Player player) throws IllegalArgumentException, IllegalStateException {
        game.getPlayers().addPlayer(player);
    }

    @Override
    public void removePlayer(Game game, Player player) throws IllegalArgumentException {
        game.getPlayers().removePlayer(player);
    }
}
