package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;


public class GathererCharacterCard extends CharacterCard {


    public GathererCharacterCard(String name, Era era, int minNumPlayers) {
        super(name, era, minNumPlayers, CharacterType.GATHERER);
    }
}
