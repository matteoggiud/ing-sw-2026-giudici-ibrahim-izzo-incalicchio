package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamelogic.ActionResolver;
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
     * observers are notified about whose turn it is to resolve the next action.
     *
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        actionResolver.resolveAutomaticSkips(game);

        if (game.getBoard().getOfferTrack().getBoxes().stream().allMatch(box -> box.isFree())) {
            nextState(game);
            return;
        }

        Player currentPlayer = game.getBoard().getOfferTrack().getCurrentPlayer();
        game.getObserverManager().notifyObservers(
                new ResolveActionTurnChangedEvent(game.getGameId(), currentPlayer.getNickname())
        );
    }

    /**
     * Advances the game to the extra action phase.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        ExtraActionState extraActionState = new ExtraActionState();
        game.setGameState(extraActionState);
        extraActionState.executePhase(game);
    }

    /**
     * Resolves the current player's action and updates the turn if needed.
     *
     * @param game the current game context
     * @param player the player resolving the action
     * @param selectedCard the selected card
     * @param fromUpperRow true if the card comes from the upper row, false otherwise
     */
    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        actionResolver.resolveRegularAction(game, player, selectedCard, fromUpperRow);
        executePhase(game);
    }
}
