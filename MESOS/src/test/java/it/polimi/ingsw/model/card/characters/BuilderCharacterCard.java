package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;

public class BuilderCharacterCard extends CharacterCard {
    private final int endGamePP;
    private final int foodDiscount;

    public BuilderCharacterCard(String name, Era era, int minNumPlayers, int endGamePP, int foodDiscount) {
        super(name, era, minNumPlayers, CharacterType.BUILDER);
        this.endGamePP = endGamePP;
        this.foodDiscount = foodDiscount;
    }

    public int getEndGamePP() {
        return endGamePP;
    }
    public int getFoodDiscount() {
        return foodDiscount;
    }

}
