package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.CharacterCard;
import it.polimi.ingsw.Model.Card.CharacterType;
import it.polimi.ingsw.Model.Card.Era;

import java.util.UUID;

public class HunterCharacterCard extends CharacterCard {
    private final boolean isFoodIcon;

    public HunterCharacterCard(UUID cardId, String name, Era era, int minNumPlayers, boolean isFoodIcon) {
        super(cardId, name, era, minNumPlayers, CharacterType.HUNTER);
        this.isFoodIcon = isFoodIcon;
    }

    public boolean isFoodIcon() {
        return isFoodIcon;
    }
}
