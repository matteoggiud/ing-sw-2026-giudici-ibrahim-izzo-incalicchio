package it.polimi.ingsw.model.ruleset;

import it.polimi.ingsw.model.board.OFFER_BOX_ID;
import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.board.TurnOrderSlot;
import it.polimi.ingsw.model.card.Era;

import java.util.ArrayList;

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
     * Determines the maximum number of buildings a player can have on the board
     * based on the current era and player count.
     * @param era the era for which the limit is requested.
     * @param numPlayers the current number of players.
     * @return
     */
    @Override
    public int getBuildingLimit(Era era, int numPlayers) {
        if (era == null) {
            throw new IllegalArgumentException("Era cannot be null");
        }
        validatePlayerCount(numPlayers);

        if (numPlayers <= 3) {
            if (era == Era.ERA_1) return 2;
            if (era == Era.ERA_2) return 2;
            return 4;
        } else if (numPlayers == 4) {
            if (era == Era.ERA_1) return 3;
            if (era == Era.ERA_2) return 2;
            return 4;
        } else {
            if (era == Era.ERA_1) return 3;
            if (era == Era.ERA_2) return 2;
            return 5;
        }
    }

    /**
     * Creates and initializes the list of OfferBoxes for the OfferTrack based on the player count.
     * The boxes are added in strict alphabetical order (A to G) to represent their position on the track.
     *
     * @param numPlayers the current number of players.
     * @return an ordered list of initialized OfferBox objects.
     */
    @Override
    public ArrayList<OfferBox> createOfferBoxes(int numPlayers) {
        validatePlayerCount(numPlayers);

        ArrayList<OfferBox> boxes = new ArrayList<>();

        // TESSERA A: Solo per 5 giocatori.
        if (numPlayers == 5) {
            boxes.add(new OfferBox(OFFER_BOX_ID.A, 3, 0, 0));
        }

        // TESSERA B: Sempre presente.
        boxes.add(new OfferBox(OFFER_BOX_ID.B, 0, 0, 1));

        // TESSERA C: Sempre presente.
        boxes.add(new OfferBox(OFFER_BOX_ID.C, 0, 1, 0));

        // TESSERA D: Solo per 3+ giocatori.
        if (numPlayers >= 3) {
            boxes.add(new OfferBox(OFFER_BOX_ID.D, 0, 0, 2));
        }

        // TESSERA E: Sempre presente.
        boxes.add(new OfferBox(OFFER_BOX_ID.E, 0, 1, 1));

        // TESSERA F: Sempre presente.
        boxes.add(new OfferBox(OFFER_BOX_ID.F, 0, 2, 0));

        // TESSERA G: Solo per 4+ giocatori.
        if (numPlayers >= 4) {
            boxes.add(new OfferBox(OFFER_BOX_ID.G, 0, 2, 1));
        }

        return boxes;
    }


    /**
     * Helper method to ensure the player count is within valid bounds.
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
}
