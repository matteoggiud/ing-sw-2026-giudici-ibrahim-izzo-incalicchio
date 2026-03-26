package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.Era;

import java.util.UUID;

public class ShamanCharacterCard extends CharacterCard {
    private final int shamanStars;


    public ShamanCharacterCard(UUID cardId, String name, Era era, int minNumPlayers, int  shamanStars) {
        super(cardId, name, era, minNumPlayers,  CharacterType.SHAMAN);
        this.shamanStars = shamanStars;
    }

    public int getShamanStars() {
        return shamanStars;
    }
}
