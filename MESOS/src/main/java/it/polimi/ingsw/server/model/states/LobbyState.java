package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.observer.event.GameStartedEvent;
import it.polimi.ingsw.server.model.observer.event.PlayersChangedEvent;
import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;

import java.sql.SQLException;

public class LobbyState implements IGameState {
    /**
     * Transitions the game from the lobby state to the setup state, allowing the game to start.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        game.setGameState(new SetupState());

        // game.getObserverManager().notifyObservers(new IGameEvent(GameEventType.GAME_STARTED, game.getGameId(), null, "Game has started"));
    }

    /**
     * Adds a player to the game lobby and notifies observers of the new player joining.
     *
     * @param game   the current game context
     * @param player the player to be added to the game
     * @throws IllegalArgumentException if the player is null or already in the game
     * @throws IllegalStateException    if the game is not in a state that allows adding players (e.g., if the game has already started)
     */
    @Override
    public void addPlayer(Game game, Player player) throws IllegalArgumentException, IllegalStateException {
        game.getPlayers().addPlayer(player);

        game.getObserverManager().notifyObservers(new PlayersChangedEvent(game.getPlayers().getPlayerNicknames()));
    }

    /**
     * Removes a player from the game lobby and notifies observers of the player leaving.
     *
     * @param game   the current game context
     * @param player the player to be removed from the game
     * @throws IllegalArgumentException if the player is null or not in the game
     */
    @Override
    public void removePlayer(Game game, Player player) throws IllegalArgumentException {
        game.getPlayers().removePlayer(player);

        game.getObserverManager().notifyObservers(new PlayersChangedEvent(game.getPlayers().getPlayerNicknames()));
    }

    /**
     * Checks if a player can join the game lobby based on the current number of players and the maximum allowed players defined in the standard ruleset.
     *
     * @param game the current game context
     * @return true if the game lobby is joinable (i.e., has fewer players than the maximum), false otherwise
     */
    @Override
    public boolean isJoinable(Game game) {
        return game.getNumberOfPlayers() < StandardRuleSet.MAX_PLAYERS;
    }

    /**
     * Starts the game by transitioning to the next state (SetupState) and notifying observers that the game has started.
     *
     * @param game the current game context
     */
    @Override
    public void startGame(Game game) {
        DataWriting db = new  DataWriting();
        db.addNewGame(game);
        nextState(game);
        game.getObserverManager().notifyObservers(new GameStartedEvent(game.getPlayers().getPlayerNicknames(), game.getGameId()));
    }
}
