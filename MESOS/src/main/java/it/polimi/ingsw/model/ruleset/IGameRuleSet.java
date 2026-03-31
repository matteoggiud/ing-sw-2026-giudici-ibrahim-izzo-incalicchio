package it.polimi.ingsw.model.ruleset;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.board.TurnOrderSlot;

import java.util.ArrayList;


/**
 * Defines the configuration and dynamic rules for a standard game of Mesos.
 * Like those based on the number of players.
 */

public interface IGameRuleSet {

    /**
     * Gets the minimum number of players required to start a game.
     *
     * @return the minimum player count.
     */
    int getMinPlayers();

    /**
     * Creates and initializes the list of slots for the Turn Order Box
     * based on the player count.
     *
     * @param numPlayers the current number of players.
     * @return a list of initialized TurnOrderSlot objects.
     * @throws IllegalArgumentException if the player count is invalid.
     */
    ArrayList<TurnOrderSlot> createTurnOrderSlots(int numPlayers);

    /**
     * Creates and initializes the list of OfferBoxes for the OfferTrack based on the player count.
     * @param numPlayers the current number of players.
     * @return an ordered list of initialized OfferBox objects.
     */
    ArrayList<OfferBox> createOfferBoxes(int numPlayers);

    /**
     * Gets the maximum number of players allowed in a game.
     *
     * @return the maximum player count.
     */
    int getMaxPlayers();

    /**
     * Calculates the number of cards to be placed in the upper offer row.
     *
     * @param numPlayers the current number of players in the game.
     * @return the capacity of the upper row.
     * @throws IllegalArgumentException if the player count is invalid.
     */
    int getUpperRowSize(int numPlayers);

    /**
     * Calculates the number of cards to be placed in the lower offer row.
     *
     * @param numPlayers the current number of players in the game.
     * @return the capacity of the lower row.
     * @throws IllegalArgumentException if the player count is invalid.
     */
    int getLowerRowSize(int numPlayers);

    /**
     * Determines the starting amount of food for a player based on their initial turn order.
     *
     * @param turnOrderIndex the 0-based index of the player's turn order (e.g., 0 is the first player).
     * @return the starting food amount.
     * @throws IllegalArgumentException if the turn order index is out of bounds.
     */
    int getInitialFood(int turnOrderIndex);
}