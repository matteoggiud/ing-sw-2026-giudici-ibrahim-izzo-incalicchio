package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;


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
