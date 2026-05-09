package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamelogic.ActionResolver;
import it.polimi.ingsw.server.model.observer.dto.mapper.BoardDTOMapper;
import it.polimi.ingsw.server.model.observer.dto.mapper.PlayerInfosDTOMapper;
import it.polimi.ingsw.server.model.observer.event.BoardUpdatedEvent;
import it.polimi.ingsw.server.model.observer.event.PlayerInfosChangedEvent;
import it.polimi.ingsw.server.model.observer.event.ResolveActionTurnChangedEvent;


/**
 * Represents the state of the game where players resolve their actions
 * after placing their totems on the offer track.
 */
public class ResolveActionsState implements IGameState {

    private final ActionResolver actionResolver = new ActionResolver();

    /**
     * Executes the action resolution phase.
     * Automatic skips are processed first. If there are still players on the offer track,
     * observers are notified about the current board state and whose turn it is next.
     *
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        actionResolver.resolveAutomaticSkips(game);
        notifyPlayerInfosUpdate(game);

        if (game.getBoard().getOfferTrack().getBoxes().stream().allMatch(box -> box.isFree())) {
            nextState(game);
            return;
        }

        notifyBoardUpdate(game);
        notifyCurrentResolvingPlayer(game);
    }

    /**
     * Advances the game to the extra action phase.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        game.setGameState(new ExtraActionState());
        game.getGameState().executePhase(game);
    }

    /**
     * Resolves the current player's action and updates the board, resources, deck and turn if needed.
     *
     * @param game         the current game context
     * @param player       the player resolving the action
     * @param selectedCard the selected card
     * @param fromUpperRow true if the card comes from the upper row, false otherwise
     */
    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        actionResolver.resolveRegularAction(game, player, selectedCard, fromUpperRow);

        notifyPlayerInfosUpdate(game);

        executePhase(game);
    }


    /**
     * Notifies observers about the updated board state.
     *
     * @param game the current game context
     */
    private void notifyBoardUpdate(Game game) {
        game.getObserverManager().notifyObservers(
                new BoardUpdatedEvent(game.getGameId(), BoardDTOMapper.toBoardDTO(game))
        );
    }

    /**
     * Notifies observers about the player who must resolve the next action.
     *
     * @param game the current game context
     */
    private void notifyCurrentResolvingPlayer(Game game) {
        Player currentPlayer = game.getBoard().getOfferTrack().getCurrentPlayer();
        game.getObserverManager().notifyObservers(
                new ResolveActionTurnChangedEvent(game.getGameId(), currentPlayer.getNickname())
        );
    }


    private void notifyPlayerInfosUpdate(Game game) {
        game.getObserverManager().notifyObservers(
                new PlayerInfosChangedEvent(
                        game.getGameId(),
                        PlayerInfosDTOMapper.toPlayerInfoDTOList(game.getPlayers().getPlayers())
                )
        );
    }

    @Override
    public boolean isEnded() {
        return false;
    }

}
