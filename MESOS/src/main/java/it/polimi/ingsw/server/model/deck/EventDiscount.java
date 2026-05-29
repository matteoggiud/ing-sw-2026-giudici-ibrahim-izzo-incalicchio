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

    /**
     * This method allows to add a food discount to the event discount.
     * @param food the food discount value to be added to the event discount.
     */
    public void addFood(int food) {
        this.food += food;
    }

    /**
     * This method allows to add a PP discount to the event discount.
     * @param PP the PP discount value to be added to the event discount.
     */
    public void addPP(int PP) {
        this.PP += PP;
    }

    /**
     * This method allows to get the PP discount value of the event discount.
     * @return the PP discount value of the event discount.
     */
    public int getPP() {
        return PP;
    }

    /**
     * This method allows to get the food discount value of the event discount.
     * @return the food discount value of the event discount.
     */
    public int getFood() {
        return food;
    }
}
