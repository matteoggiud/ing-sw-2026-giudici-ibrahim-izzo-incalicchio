package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.core.Player;

/**
 * Represents the offer box on the board.
 * Players can place their totems on an offer box to choose cards / get reward.
 */
public class OfferBox {
    /**
     * Enumeration representing the unique identifiers for the offer boxes on the board.
     */
    private final OFFER_BOX_ID boxId;

    /**
     * Food reward associated with the offer box (only one box).
     */
    private final int foodReward;

    /**
     * Number of picks allowed to this box from the upper row.
     */
    private final int upperRowPicks;

    /**
     * Number of picks allowed to this box from the lower row.
     */
    private final int lowerRowPicks;

    /**
     * Number of picks remaining from the upper row for this offer box (initially equal to upperRowPicks).
     */
    private int remainingUpperPicks;

    /**
     * Number of picks remaining from the lower row for this offer box (initially equal to lowerRowPicks).
     */
    private int remainingLowerPicks;

    /**
     * The player who has placed their totem on this offer box, or null if the box is currently free.
     */
    private Player placedPlayer;

    /**
     * Constructs an OfferBox with the specified attributes.
     *
     * @param boxId         the unique identifier for the offer box
     * @param foodReward    the amount of food reward associated with this offer box (=3 || =0)
     * @param upperRowPicks the number of picks allowed from the upper row associated with this offer box
     * @param lowerRowPicks the number of picks allowed from the lower row associated with this offer box
     * @throws IllegalArgumentException if boxId is null, if foodReward is not 0 or 3, or if upperRowPicks or lowerRowPicks are negative
     */

    public OfferBox(OFFER_BOX_ID boxId, int foodReward, int upperRowPicks, int lowerRowPicks) throws IllegalArgumentException {
        if (boxId == null) throw new IllegalArgumentException("boxId cannot be null");

        if (foodReward != 0 && foodReward != 3) throw new IllegalArgumentException("Food reward must be either 0 or 3");

        if (upperRowPicks < 0 || lowerRowPicks < 0) throw new IllegalArgumentException("Row's picks must be >= 0");

        this.boxId = boxId;
        this.foodReward = foodReward;
        this.upperRowPicks = upperRowPicks;
        this.lowerRowPicks = lowerRowPicks;
    }

    /**
     * Gets the box id associated with this offer box.
     *
     * @return the unique identifier for this offer box.
     */
    public OFFER_BOX_ID getBoxId() {
        return boxId;
    }

    /**
     * Gets the food reward associated with this offer box.
     *
     * @return the amount of food reward provided by this offer box (either 0 or 3).
     */
    public int getFoodReward() {
        return foodReward;
    }

    /**
     * Gets the number of picks allowed from the upper row associated with this offer box.
     *
     * @return the number of picks allowed from the upper row for this offer box.
     */
    public int getUpperRowPicks() {
        return upperRowPicks;
    }

    /**
     * Gets the number of picks allowed from the lower row associated with this offer box.
     *
     * @return the number of picks allowed from the lower row for this offer box.
     */
    public int getLowerRowPicks() {
        return lowerRowPicks;
    }

    /**
     * Gets the player who has placed their totem on this offer box.
     *
     * @return the player currently placed on this offer box, or null if the box is free.
     */
    public Player getPlacedPlayer() {
        return placedPlayer;
    }

    /**
     * Checks if the offer box is currently free (i.e., no player has placed their totem on it).
     *
     * @return true if the offer box is free, false otherwise.
     */
    public boolean isFree() {
        return placedPlayer == null;
    }

    /**
     * Places a player on the offer box, if the offer box is not already occupied by another player.
     *
     * @param player the player to be placed on the offer box
     * @throws IllegalStateException    if the offer box is already occupied by another player
     * @throws IllegalArgumentException if the player is null
     */ //da capire come gestire quando l'offerbox ti da 3 di cibo, chi deve gestirlo??
    public void placePlayer(Player player) throws IllegalStateException, IllegalArgumentException {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");

        if (!isFree()) throw new IllegalStateException("Offer box is already occupied");

        this.placedPlayer = player;
    }

    /**
     * Checks if the player can still pick cards from the upper row based on the remaining picks for this offer box.
     *
     * @return true if the player can still pick from the upper row, false otherwise.
     */
    public boolean canPickUpper() {
        return remainingUpperPicks > 0;
    }

    /**
     * Checks if the player can still pick cards from the lower row based on the remaining picks for this offer box.
     *
     * @return true if the player can still pick from the lower row, false otherwise.
     */
    public boolean canPickLower() {
        return remainingLowerPicks > 0;
    }

    /**
     * Consumes a pick from either the upper or lower row based on the specified row, and updates the remaining picks accordingly.
     * @param isUpperRow true if the pick is from the upper row, false if it's from the lower row
     */
    public void consumePick(boolean isUpperRow) {
        if (isUpperRow) {
            if (remainingUpperPicks <= 0) {
                throw new IllegalStateException("No upper picks left");
            }
            remainingUpperPicks--;
        } else {
            if (remainingLowerPicks <= 0) {
                throw new IllegalStateException("No lower picks left");
            }
            remainingLowerPicks--;
        }
    }

    /**
     * Checks if the player has completed all their picks from both the upper and lower rows based on the remaining picks for this offer box.
     * @return true if the player has completed all picks (i.e., no remaining picks for both rows), false otherwise.
     */
    public boolean hasCompletedAllPicks() {
        return remainingUpperPicks == 0 && remainingLowerPicks == 0;
    }

    /**
     * Removes the player currently placed on the offer box and returns that player.
     *
     * @return the removed player, or null if the offer box was already free.
     */
    public Player removePlayer() {
        Player removed = placedPlayer;
        this.placedPlayer = null;
        return removed;
    }
}
