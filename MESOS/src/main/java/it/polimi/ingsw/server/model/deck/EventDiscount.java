package it.polimi.ingsw.server.model.deck;

public class EventDiscount {
    int PP;
    int food;

    /**
     * Constructor for the EventDiscount class,
     * it initializes the event discount with the provided PP and food values.
     * @param PP the PP discount value to be applied to the player.
     * @param food the food discount value to be applied to the player.
     */
    public EventDiscount(int PP, int food) {
        this.PP = PP;
        this.food = food;
    }

    public void addFood(int food) {
        this.food += food;
    }

    public void addPP(int PP) {
        this.PP += PP;
    }

    public int getPP() {
        return PP;
    }
    public int getFood() {
        return food;
    }
}
