package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;


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
