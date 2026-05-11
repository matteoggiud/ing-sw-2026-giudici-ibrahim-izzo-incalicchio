package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.mapper.GameInfoDTOMapper;
import it.polimi.ingsw.server.model.observer.event.GameStartedEvent;
import it.polimi.ingsw.server.model.observer.event.JoinedGameEvent;
import it.polimi.ingsw.server.model.observer.event.PlayersChangedEvent;

public class LobbyState implements IGameState {
    /**
     * Transitions the game from the lobby state to the setup state, allowing the game to start.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        game.setGameState(new SetupState());
        game.getGameState().executePhase(game);
    }

    /**
     * Adds a player to the game lobby if the lobby is joinable
     *
     * @param game   the current game context
     * @param player the player who wants to join
     * @throws IllegalArgumentException if the player is null or already in the game
     * @throws IllegalStateException    if the lobby is full and cannot accept more players
     */
    @Override
    public void addPlayer(Game game, Player player) throws IllegalArgumentException, IllegalStateException {
        if (!isJoinable(game)) {
            throw new IllegalStateException("Lobby is full.");
        }

        game.getPlayers().addPlayer(player);
        notifyPlayersChanged(game);
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
        notifyPlayersChanged(game);
    }

    /**
     * Checks if the game lobby is joinable by comparing the current number of players
     * with the expected player count.
     *
     * @param game the current game context
     * @return true if the game lobby is joinable (i.e., has fewer players than the maximum), false otherwise
     */
    @Override
    public boolean isJoinable(Game game) {
        return game.getNumberOfPlayers() < game.getExpectedPlayerCount();
    }

    /**
     * Starts the game by transitioning to the next state (SetupState) and notifying observers that the game has started.
     *
     * @param game the current game context
     */
    @Override
    public void startGame(Game game) {
        DataWriting dw = new DataWriting();
        for (String player : game.getPlayers().getPlayerNicknames())
            dw.joinExistingGame(game.getGameId(), player);

        notifyGameStarted(game);
        nextState(game);
    }

    /**
     * Notifies observers about the updated list of players in the lobby.
     *
     * @param game the current game context
     */
    private void notifyPlayersChanged(Game game) {
        game.getObserverManager().notifyObservers(
                new JoinedGameEvent(GameInfoDTOMapper.toGameInfoDTO(game))
        );
    }

    /**
     * Notifies observers that the game has started.
     *
     * @param game the current game context
     */
    private void notifyGameStarted(Game game) {
        game.getObserverManager().notifyObservers(
                new GameStartedEvent(game.getPlayers().getPlayerNicknames(), game.getGameId())
        );
    }

    @Override
    public boolean isEnded() {
        return false;
    }
}
