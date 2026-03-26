package it.polimi.ingsw.Model.Board;
import it.polimi.ingsw.Model.Player;

/**
 * Represents the offer box on the board. Each offer box has a unique ID, a food reward,
 * and a certain number of picks for the upper and lower rows.
 * Players can place their totems on an offer box to choose cards / get reward.
 */

public class OfferBox {
    private OfferBoxId id;
    private final int foodReward;
    private final int upperRowPicks;
    private final int lowerRowPicks;
    private Player placedPlayer;

    /**
     *
     * @param id the unique identifier for the offer box
     * @param foodReward the amount of food reward associated with this offer box
     * @param upperRowPicks the number of picks allowed from the upper row associated with this offer box
     * @param lowerRowPicks the number of picks allowed from the lower row associated with this offer box
     */

    public OfferBox(OfferBoxId id, int foodReward, int upperRowPicks, int lowerRowPicks) {
        this.id = id;
        this.foodReward = foodReward;
        this.upperRowPicks = upperRowPicks;
        this.lowerRowPicks = lowerRowPicks;
    }

    /**
     *
     * @return getters for the offer box attributes
     */

    public OfferBoxId getId() {
        return id;
    }
    public int getFoodReward() {
        return foodReward;
    }
    public int getUpperRowPicks() {
        return upperRowPicks;
    }
    public int getLowerRowPicks() {
        return lowerRowPicks;
    }
    public Player getPlacedPlayer() {
        return placedPlayer;
    }

    /**
     *
     * @return checks if the offer box is currently free (i.e., no player has placed their totem on it)
     */

    public boolean isFree() {
        return placedPlayer == null;
    }

    /**
     * @param player the player to be placed on the offer box
     * @throws IllegalStateException if the offer box is already occupied by another player
     * This method allows a player to place their totem on the offer box.
     * If the offer box is already occupied,
     * it throws an exception to indicate that the action cannot be performed.
     */

    public void placePlayer(Player player){
        if(!isFree()) throw new IllegalStateException("Offer box is already occupied");
        this.placedPlayer = player;
    }

    /**
     *
     * @return removes the player from the offer box and returns the removed player.
     * If the offer box is already free, it returns null.
     */

    public Player removePlayer() {
        Player removed = placedPlayer;
        this.placedPlayer = null;
        return removed;
    }
}
