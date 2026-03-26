package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.CharacterCard;
import it.polimi.ingsw.Model.Card.CharacterType;
import it.polimi.ingsw.Model.Card.Era;


public class GathererCharacterCard extends CharacterCard {


    public GathererCharacterCard(String name, Era era, int minNumPlayers) {
        super(name, era, minNumPlayers, CharacterType.GATHERER);
    }
}
