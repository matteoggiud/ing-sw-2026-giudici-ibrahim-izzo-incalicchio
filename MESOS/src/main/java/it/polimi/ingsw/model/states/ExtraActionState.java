package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.building.extraction.IExtraActionEffect;
import it.polimi.ingsw.model.gamelogic.ActionResolver;

/**
 *
 */
public class ExtraActionState implements IGameState {

    /**
     * Advances the game to the next logical state.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        game.setGameState(new EndRoundState());
    }

    /**
     * Executes automatic logic for the current phase.
     *
     */
    @Override
    public boolean getExtraAction(Player player) {
        return player.getPlayerDeck().getActiveExtractionEffects().stream()
                .anyMatch(IExtraActionEffect::hasExtraAction);
    }

    /**
     * Picks a card from upper row
     *
     * @param game         the current game context
     * @param player       the player making the action
     * @param selectedCard the card the player wants to pick
     * @param fromUpperRow true if the card is from upper row, false if it's from lower row
     * @throws IllegalStateException if the player doesn't have any extra action to resolve
     */
    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        if (!getExtraAction(player)) {
            throw new IllegalStateException("You don't have any extra action to resolve.");
        }
        ActionResolver resolver = new ActionResolver();
        resolver.resolveExtraAction(game, player, selectedCard, fromUpperRow);
    }

}
