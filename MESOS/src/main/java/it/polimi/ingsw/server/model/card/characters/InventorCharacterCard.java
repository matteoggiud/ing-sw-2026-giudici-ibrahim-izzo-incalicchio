package it.polimi.ingsw.server.model.card.characters;

import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.card.Era;
import it.polimi.ingsw.server.model.deck.PlayerDeck;

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
     * @param cardId        the unique identifier of the card
     * @param name          the name of the card
     * @param era           the era of the card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @param inventorType  the specific type of Inventor this card represents (must not be null)
     * @param imageKey      the key used to retrieve the card's image from the resource manager
     * @throws IllegalArgumentException if numeric constraints are violated or if something is null
     */
    public InventorCharacterCard(int cardId, String name, Era era, int minNumPlayers, InventorType inventorType, String imageKey) throws IllegalArgumentException {
        super(cardId, name, era, minNumPlayers,  imageKey);
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

    @Override
    public void onAddToPlayerDeck(PlayerDeck playerDeck) {
        playerDeck.incrementTypeCount(CharacterType.INVENTOR);
    }
}
