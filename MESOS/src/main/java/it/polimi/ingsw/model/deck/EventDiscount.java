package it.polimi.ingsw.model.deck;

public class EventDiscount {
    int PP;
    int food;

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
