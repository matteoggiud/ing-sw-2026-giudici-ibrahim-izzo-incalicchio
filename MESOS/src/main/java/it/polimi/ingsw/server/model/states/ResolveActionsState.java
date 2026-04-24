package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamelogic.ActionResolver;

/**
 * The ResolveActionsState class represents the state of the game where players resolve their
 * actions after placing their totems on the offer track.
 */
public class ResolveActionsState implements IGameState {

    private final ActionResolver actionResolver = new ActionResolver();

    @Override
    public void executePhase(Game game) {
        actionResolver.resolveAutomaticSkips(game);

        if (game.getBoard().getOfferTrack().getBoxes().stream().allMatch(box -> box.isFree())) {
            nextState(game);
        }
    }

    /**
     * Advances the game to the next logical state, which is the ExtraActionState
     * where players can resolve a possible extra actions they have.
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        ExtraActionState extraActionState = new ExtraActionState();
        game.setGameState(extraActionState);
        extraActionState.executePhase(game);
    }


    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        actionResolver.resolveRegularAction(game, player, selectedCard, fromUpperRow);
        executePhase(game);
    }
}
