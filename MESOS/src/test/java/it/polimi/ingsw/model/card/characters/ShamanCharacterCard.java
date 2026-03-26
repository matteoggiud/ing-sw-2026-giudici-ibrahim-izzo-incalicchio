package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;


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
