package it.polimi.ingsw.model.ruleset;

import it.polimi.ingsw.model.board.TurnOrderSlot;

import java.util.ArrayList;

/**
 * Defines the configuration and dynamic rules for a standard game of Mesos.
 * Like those based on the number of players.
 */

public class StandardRuleSet implements IGameRuleSet {

    /**
     * Gets the minimum number of players required to start a game.
     * @return the minimum player count.
     */
    @Override
    public int getMinPlayers() {
        return 2;
    }

    /**
     * Gets the maximum number of players allowed in a game.
     * @return the maximum player count.
     */
    @Override
    public int getMaxPlayers() {
        return 5;
    }

    /**
     * Calculates the number of cards to be placed in the upper offer row.
     * @param numPlayers the current number of players in the game.
     * @return the capacity of the upper row.
     */
    @Override
    public int getUpperRowSize(int numPlayers) {
        validatePlayerCount(numPlayers);
        return numPlayers + 4;
    }

    /**
     * Calculates the number of cards to be placed in the lower offer row.
     * @param numPlayers the current number of players in the game.
     * @return the capacity of the lower row.
     */
    @Override
    public int getLowerRowSize(int numPlayers) {
        validatePlayerCount(numPlayers);
        return numPlayers + 1;
    }

    /**
     * Determines the starting amount of food for a player based on their initial turn order.
     * @param turnOrderIndex the 0-based index of the player's turn order (e.g., 0 is the first player).
     * @return the initial amount of food for the player based on their turn order.
     */
    @Override
    public int getInitialFood(int turnOrderIndex) {
        if (turnOrderIndex < 0 || turnOrderIndex >= getMaxPlayers()) {
            throw new IllegalArgumentException("Invalid turn order index: " + turnOrderIndex);
        }

        return switch (turnOrderIndex) {
            case 0 -> 2;
            case 1, 2 -> 3;
            case 3, 4 -> 4;
            default -> throw new IllegalStateException("Unexpected turn order index: " + turnOrderIndex);
        };
    }

    /**
     * Creates and initializes the list of slots for the Turn Order Box based on the player count.
     * @param numPlayers the current number of players.
     * @return a list of initialized TurnOrderSlot objects for the Turn Order Box.
     */
    @Override
    public ArrayList<TurnOrderSlot> createTurnOrderSlots(int numPlayers) {
        validatePlayerCount(numPlayers);

        ArrayList<TurnOrderSlot> slots = new ArrayList<>();

        switch (numPlayers) {
            case 2 -> {
                slots.add(new TurnOrderSlot(1));
                slots.add(new TurnOrderSlot(-1));
            }
            case 3 -> {
                slots.add(new TurnOrderSlot(2));
                slots.add(new TurnOrderSlot(0));
                slots.add(new TurnOrderSlot(-1));
            }
            case 4 -> {
                slots.add(new TurnOrderSlot(2));
                slots.add(new TurnOrderSlot(1));
                slots.add(new TurnOrderSlot(0));
                slots.add(new TurnOrderSlot(-1));
            }
            case 5 -> {
                slots.add(new TurnOrderSlot(3));
                slots.add(new TurnOrderSlot(1));
                slots.add(new TurnOrderSlot(0));
                slots.add(new TurnOrderSlot(0));
                slots.add(new TurnOrderSlot(-1));
            }
        }

        return slots;
    }

    //DA FARE: REGOLA PER TESSERE OFFERTE

    //DA FARE: REGOLA PER COSTRUZIONE DECK


    /**
     * Helper method to ensure the player count is within valid bounds.
     * @param numPlayers the number of players to check.
     * @throws IllegalArgumentException if the player count is outside the allowed range.
     */
    private void validatePlayerCount(int numPlayers) {
        if (numPlayers < getMinPlayers() || numPlayers > getMaxPlayers()) {
            throw new IllegalArgumentException(
                    "Player count must be between " + getMinPlayers() + " and " + getMaxPlayers() + ". Provided: " + numPlayers
            );
        }
    }
}
