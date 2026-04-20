package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.building.extraction.IExtraActionEffect;

import java.util.ArrayList;

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
    public int getExtraAction(Player player) {
        return (int) player.getPlayerDeck().getActiveExtractionEffects().stream()
                .filter(IExtraActionEffect::hasExtraAction)
                .count();
    }

    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        // TODO: implement this method
    }

}
