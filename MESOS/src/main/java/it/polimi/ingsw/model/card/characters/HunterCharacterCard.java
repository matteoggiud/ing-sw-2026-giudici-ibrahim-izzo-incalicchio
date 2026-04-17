package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;

/**
 * Represents a Hunter Character Card in the game.
 */
public class HunterCharacterCard extends CharacterCard {
    /** Indicates whether this Hunter card has a food icon. */
    private final boolean isFoodIcon;

    /**
     * Constructs a new HunterCharacterCard with the specified attributes.
     *
     * @param name the name of the card
     * @param era the era of the card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @param isFoodIcon indicates whether this Hunter card has a food icon (true if it does, false otherwise)
     * @throws IllegalArgumentException if numeric constraints are violated or if something is null
     */
    public HunterCharacterCard(String name, Era era, int minNumPlayers, boolean isFoodIcon) throws IllegalArgumentException {
        super(name, era, minNumPlayers);
        this.isFoodIcon = isFoodIcon;
    }


    /**
     * Indicates that this card is a Hunter character card.
     *
     * @return true, since this is a Hunter character card.
     */
    @Override
    public boolean isHunter() {
        return true;
    }

    /**
     * Checks if this Hunter card has a food icon.
     *
     * @return true if this Hunter card has a food icon, false otherwise.
     */
    @Override
    public boolean hasFoodIcon() {
        return isFoodIcon;
    }


}
