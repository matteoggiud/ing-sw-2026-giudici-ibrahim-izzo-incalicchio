package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.CharacterCard;
import it.polimi.ingsw.Model.Card.CharacterType;
import it.polimi.ingsw.Model.Card.Era;


public class ArtistCharacterCard extends CharacterCard {

    public ArtistCharacterCard(String name, Era era, int minNumPlayers) {
        super(name, era, minNumPlayers, CharacterType.ARTIST);
    }
}
