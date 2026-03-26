package it.polimi.ingsw.Model.Card;

import it.polimi.ingsw.Model.Effects.BuildingEffect.IBuildingEffect;


public class BuildingCard extends Card {
    private final int foodCost;
    private final int endGamePP;
    private final IBuildingEffect effect;

    public BuildingCard(String name, Era era, int minNumPlayers, int foodCost, int endGamePP, IBuildingEffect effect) {
        super(name, era, minNumPlayers);
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

    public IBuildingEffect getEffect(){
        return effect;
    }


}
