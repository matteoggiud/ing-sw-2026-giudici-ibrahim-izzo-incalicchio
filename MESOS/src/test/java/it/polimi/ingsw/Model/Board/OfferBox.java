package it.polimi.ingsw.Model.Board;
import it.polimi.ingsw.Model.Player;

/* NEED TO IMPORT PLAYER */

public class OfferBox {
    private OfferBoxId id;
    private final int foodReward;
    private final int upperRowPicks;
    private final int lowerRowPicks;
    private Player placedPlayer;

    public OfferBox(OfferBoxId id, int foodReward, int upperRowPicks, int lowerRowPicks) {
        this.id = id;
        this.foodReward = foodReward;
        this.upperRowPicks = upperRowPicks;
        this.lowerRowPicks = lowerRowPicks;
    }

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

    public boolean isFree() {
        return placedPlayer == null;
    }

    public void placePlayer(Player player){
        if(!isFree()) throw new IllegalStateException("Offer box is already occupied");
        this.placedPlayer = player;
    }

    public Player removePlayer() {
        Player removed = placedPlayer;
        this.placedPlayer = null;
        return removed;
    }
}
