package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.CharacterCard;
import it.polimi.ingsw.Model.Card.CharacterType;
import it.polimi.ingsw.Model.Card.Era;


public class ShamanCharacterCard extends CharacterCard {
    private final int shamanStars;


    public ShamanCharacterCard(String name, Era era, int minNumPlayers, int  shamanStars) {
        super(name, era, minNumPlayers,  CharacterType.SHAMAN);
        this.shamanStars = shamanStars;
    }

    public int getShamanStars() {
        return shamanStars;
    }
}
