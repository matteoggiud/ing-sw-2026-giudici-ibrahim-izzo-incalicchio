package it.polimi.ingsw.Model.Card;

import java.util.UUID;

public class BuildingCard extends Card {
    private final int foodCost;
    private final int endGamePP;
    private final BuildingEffect effect;

    public BuildingCard(UUID cardId, String name, Era era, int minNumPlayers, int foodCost, int endGamePP, BuildingEffect effect) {
        super(cardId, name, era, minNumPlayers);
        this.foodCost = foodCost;
        this.endGamePP = endGamePP;
        this.effect = effect;
    }

    public int getFoodCost() {
        return foodCost;
    }

    public int getEndGamePP() {
        return endGamePP;
    }

    public BuildingEffect getEffect(){
        return effect;
    }


}
