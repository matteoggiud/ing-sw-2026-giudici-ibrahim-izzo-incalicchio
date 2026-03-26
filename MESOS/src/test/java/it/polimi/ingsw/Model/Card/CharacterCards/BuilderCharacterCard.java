package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.Era;
import java.util.UUID;

public class BuilderCharacterCard extends CharacterCard {
    private final int endGamePP;
    private final int foodDiscount;

    public BuilderCharacterCard(UUID cardId, String name, Era era, int minNumPlayers, int endGamePP, int foodDiscount) {
        super(cardId, name, era, minNumPlayers, CharacterType.BUILDER);
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
