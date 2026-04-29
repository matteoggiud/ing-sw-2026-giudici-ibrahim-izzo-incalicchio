package it.polimi.ingsw.server.model.ruleset;

import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.card.EventCard;

import java.util.*;

/**
 * Defines the configuration and dynamic rules for a standard game of Mesos.
 * Like those based on the number of players.
 */

public class StandardRuleSet implements IGameRuleSet {

    /**
     * The minimum number of players allowed in a game of Mesos is 2.
     */
    public static int MIN_PLAYERS = 2;

    /**
     * The maximum number of players allowed in a game of Mesos is 5.
     */
    public static int MAX_PLAYERS = 5;


    /**
     * Calculates the number of cards to be placed in the upper offer row.
     *
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
     *
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
     *
     * @param turnOrderIndex the 0-based index of the player's turn order (e.g., 0 is the first player).
     * @return the initial amount of food for the player based on their turn order.
     */
    @Override
    public int getInitialFood(int turnOrderIndex) {
        if (turnOrderIndex < 0 || turnOrderIndex >= MAX_PLAYERS) {
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
     *
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

    /**
     * Sorts the provided ordered list of event cards into a stack, where sustenance are on top of stack
     *
     * @param eventCards the list of event cards to be sorted into a stack for the game.
     * @return an ordered Stack of EventCards
     */
    @Override
    public Stack<EventCard> sortEventCards(List<EventCard> eventCards) {
        if (eventCards == null) {
            throw new IllegalArgumentException("eventCards cannot be null");
        }

        Stack<EventCard> stack = new Stack<>();

        // Sort the event cards by the effect priority, priority 0 in top of stack, priority 1 in top
        eventCards.stream()
                .sorted(Comparator.comparingInt((EventCard e) -> e.getEventEffect().getSortPriority()).reversed())
                .forEach(stack::push);

        return stack;
    }

    @Override
    public ArrayList<OfferBox> createOfferBoxes(int numPlayers) {
        validatePlayerCount(numPlayers);

        ArrayList<OfferBox> boxes = new ArrayList<>();

        // Position 1 in 5-player games: food-only action.
        if (numPlayers == 5) {
            boxes.add(new OfferBox(3, 0, 0));
        }

        // Always present.
        boxes.add(new OfferBox(0, 0, 1));
        boxes.add(new OfferBox(0, 1, 0));

        // Present with 3+ players.
        if (numPlayers >= 3) {
            boxes.add(new OfferBox(0, 0, 2));
        }

        // Always present.
        boxes.add(new OfferBox(0, 1, 1));
        boxes.add(new OfferBox(0, 2, 0));

        // Present with 4+ players.
        if (numPlayers >= 4) {
            boxes.add(new OfferBox(0, 2, 1));
        }

        return boxes;
    }



    /**
     * Helper method to ensure the player count is within valid bounds.
     *
     * @param numPlayers the number of players to check.
     * @throws IllegalArgumentException if the player count is outside the allowed range.
     */
    private void validatePlayerCount(int numPlayers) {
        if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException(
                    "Player count must be between " + MIN_PLAYERS + " and " + MAX_PLAYERS + ". Provided: " + numPlayers
            );
        }
    }

    public static HashMap<Integer, Integer> getEra1BuildingLimits() {
        HashMap<Integer, Integer> limits = new HashMap<>();
        limits.put(2, 1);
        limits.put(3, 2);
        limits.put(4, 2);
        limits.put(5, 2);
        return limits;
    }

    public static HashMap<Integer, Integer> getEra2BuildingLimits() {
        HashMap<Integer, Integer> limits = new HashMap<>();
        limits.put(2, 2);
        limits.put(3, 2);
        limits.put(4, 3);
        limits.put(5, 3);
        return limits;
    }

    public static HashMap<Integer, Integer> getEra3BuildingLimits() {
        HashMap<Integer, Integer> limits = new HashMap<>();
        limits.put(2, 3);
        limits.put(3, 4);
        limits.put(4, 4);
        limits.put(5, 5);
        return limits;
    }
}
