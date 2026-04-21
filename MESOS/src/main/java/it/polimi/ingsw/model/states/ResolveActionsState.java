package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.gamelogic.ActionResolver;

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


    @Override
    public void nextState(Game game) {
        game.setGameState(new ExtraActionState());
    }

    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        actionResolver.resolveRegularAction(game, player, selectedCard, fromUpperRow);
        executePhase(game);
    }
}
