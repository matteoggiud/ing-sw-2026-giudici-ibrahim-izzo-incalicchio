package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.CharacterCard;
import it.polimi.ingsw.Model.Card.CharacterType;
import it.polimi.ingsw.Model.Card.Era;


public class InventorCharacterCard extends CharacterCard {
    private final InventorType inventorType;

    public InventorCharacterCard(String name, Era era, int minNumPlayers, InventorType inventorType) {
        super(name, era, minNumPlayers, CharacterType.INVENTOR);
        this.inventorType = inventorType;
    }

    public InventorType getInventorType() {
        return inventorType;
    }
}
