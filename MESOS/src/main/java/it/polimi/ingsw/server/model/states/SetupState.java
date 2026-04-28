package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamelogic.GameBuilder;
import it.polimi.ingsw.server.model.observer.dto.mapper.BoardDTOMapper;
import it.polimi.ingsw.server.model.observer.dto.mapper.PlayerDeckDTOMapper;
import it.polimi.ingsw.server.model.observer.dto.mapper.PlayerInfoDTOMapper;
import it.polimi.ingsw.server.model.observer.event.BoardUpdatedEvent;
import it.polimi.ingsw.server.model.observer.event.PlayerInfoChangedEvent;
import it.polimi.ingsw.server.model.observer.event.PlayerResourcesUpdatedEvent;


public class SetupState implements IGameState {

    @Override
    public void nextState(Game game) {
        game.setGameState(new PlacingTotemState());
        game.getGameState().executePhase(game);
    }

    /**
     * Executes the setup phase of the game.
     * After preparing the game, it notifies observers about:
     * - the current board state
     * - each player's initial resources
     *
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        GameBuilder builder = new GameBuilder(game);

        // Prepare the board, decks, turn order and initial resources.
        builder.prepareGame();

        // Notify observers about the board initialized
        notifyAll(game);

        // The setup is complete, so the next active phase is placing totems.
        nextState(game);
    }

    /**
     * Notifies observers about the updated resources of all players in the game.
     *
     * @param game the current game context
     */
    private void notifyAll(Game game) {
        for (Player player : game.getPlayers().getShuffledPlayers()) {
            game.getObserverManager().notifyObservers(new PlayerInfoChangedEvent(game.getGameId(), PlayerInfoDTOMapper.toPlayerInfoDTO(player)));
        }

        game.getObserverManager().notifyObservers(new BoardUpdatedEvent(game.getGameId(), BoardDTOMapper.toBoardDTO(game)));
    }

}
