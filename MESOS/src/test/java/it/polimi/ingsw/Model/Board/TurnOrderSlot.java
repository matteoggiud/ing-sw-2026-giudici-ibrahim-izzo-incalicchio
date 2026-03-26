package it.polimi.ingsw.Model.Board;
import it.polimi.ingsw.Model.Player;

public class TurnOrderSlot {
    private final int index;
    private final int foodBonus;
    private Player placedPlayer;

    public TurnOrderSlot(int index, int foodBonus) {
        if (index < 0) {
            throw new IllegalArgumentException("index cannot be negative");
        }
        this.index = index;
        this.foodBonus = foodBonus;
        this.placedPlayer = null;
    }

    public int getIndex() { return index; }
    public int getFoodBonus() { return foodBonus; }
    public Player getPlacedPlayer() { return placedPlayer; }

    public boolean isFree() {
        return placedPlayer == null;
    }

    public void placePlayer(Player player) {
        if (!isFree()) throw new IllegalStateException("TurnOrderSlot occupato");
        this.placedPlayer = player;
    }

    public Player removePlayer() {
        Player removed = placedPlayer;
        placedPlayer = null;
        return removed;
    }
}
