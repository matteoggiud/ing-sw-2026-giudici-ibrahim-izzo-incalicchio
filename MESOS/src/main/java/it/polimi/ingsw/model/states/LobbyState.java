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
        if (player == null) throw new IllegalArgumentException("Player is null");
        if (game.getPlayers().size() >= 5) throw new IllegalStateException("You can't add more than 5 players");
        if (game.getPlayers().contains(player)) throw new IllegalStateException("Player already in the game");

        game.getPlayers().add(player);
    }

    @Override
    public void removePlayer(Game game, Player player) throws IllegalArgumentException {
        if (player == null) throw new IllegalArgumentException("Player is null");

        game.getPlayers().remove(player);
    }
}
