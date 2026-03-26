package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.CharacterCard;
import it.polimi.ingsw.Model.Card.CharacterType;
import it.polimi.ingsw.Model.Card.Era;

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
