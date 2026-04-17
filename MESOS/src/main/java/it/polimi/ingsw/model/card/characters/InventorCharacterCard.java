package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;

import java.util.Optional;

/**
 * Represents an Inventor Character Card in the game.
 * Inventor cards provide also the type of Inventor (i.e., BOAT, ARROW, etc.).
 */
public class InventorCharacterCard extends CharacterCard {
    /**
     * The specific type of Inventor this card represents.
     */
    private final InventorType inventorType;

    /**
     * Constructs a new InventorCharacterCard with the specified attributes.
     *
     * @param name          the name of the card
     * @param era           the era of the card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @param inventorType  the specific type of Inventor this card represents (must not be null)
     * @throws IllegalArgumentException if numeric constraints are violated or if something is null
     */
    public InventorCharacterCard(String name, Era era, int minNumPlayers, InventorType inventorType) throws IllegalArgumentException {
        super(name, era, minNumPlayers);
        if (inventorType == null)
            throw new IllegalArgumentException("Inventor type must not be null");

        this.inventorType = inventorType;
    }

    /**
     * Gets the specific type of Inventor this card represents.
     *
     * @return the inventor type.
     */
    @Override
    public Optional<InventorType> getInventorType() {
        return Optional.ofNullable(inventorType);
    }

    /**
     * Indicates that this card is an Inventor character card.
     *
     * @return true, since this is an Inventor character card.
     */
    @Override
    public boolean isInventor() { return true; }
}
