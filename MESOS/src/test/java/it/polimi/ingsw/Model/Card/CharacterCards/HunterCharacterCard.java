package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.CharacterCard;
import it.polimi.ingsw.Model.Card.CharacterType;
import it.polimi.ingsw.Model.Card.Era;


public class HunterCharacterCard extends CharacterCard {
    private final boolean isFoodIcon;

    public HunterCharacterCard(String name, Era era, int minNumPlayers, boolean isFoodIcon) {
        super(name, era, minNumPlayers, CharacterType.HUNTER);
        this.isFoodIcon = isFoodIcon;
    }

    public boolean isFoodIcon() {
        return isFoodIcon;
    }
}
