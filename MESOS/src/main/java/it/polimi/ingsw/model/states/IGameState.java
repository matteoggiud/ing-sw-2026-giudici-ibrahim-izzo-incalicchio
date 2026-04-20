package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;

import java.util.ArrayList;

/**
 * Interface representing the generic state of the game.
 * Uses the State Design Pattern to handle the game flow.
 */
public interface IGameState {
    /**
     * Advances the game to the next logical state.
     *
     * @param game the current game context
     */
    void nextState(Game game);

    /**
     * Executes automatic logic for the current phase (if any).
     *
     * @param game the current game context
     */
    default void executePhase(Game game) {
    }

    /**
     * A player attempts to place their totem on the board.
     *
     * @param game     the current game context
     * @param player   the player making the action
     * @param offerBox the offer box where the player wants to place their totem
     * @throws IllegalStateException if this action is not allowed in the current state
     */
    default void placeTotem(Game game, Player player, OfferBox offerBox) {
        throw new IllegalStateException("You cannot place a totem during this phase of the game.");
    }

    default int getExtraAction(Player player){
        throw new IllegalStateException("You cannot get extra action during this phase of the game.");
    }

    default void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        throw new IllegalStateException("You cannot take card during this phase of the game.");
    }

    default void addPlayer(Game game, Player player) {}

    default void removePlayer(Game game, Player player) {}



}
