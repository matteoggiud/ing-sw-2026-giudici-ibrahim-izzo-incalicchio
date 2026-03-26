package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.CharacterCard;
import it.polimi.ingsw.Model.Card.CharacterType;
import it.polimi.ingsw.Model.Card.Era;

import java.util.UUID;

public class ArtistCharacterCard extends CharacterCard {

    public ArtistCharacterCard(UUID cardId, String name, Era era, int minNumPlayers) {
        super(cardId, name, era, minNumPlayers, CharacterType.ARTIST);
    }
}
