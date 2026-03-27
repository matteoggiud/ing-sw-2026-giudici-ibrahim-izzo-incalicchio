package it.polimi.ingsw.model.card;

/**
 * This class represents a character card, which is a type of card in the game.
 */
public class CharacterCard extends Card {

    /** The specific type of the character card (HUNTER, GATHERER, SHAMAN, exc.). */
    private final CharacterType type;

    /**
     * Construct a new CharacterCard with specific properties.
     * @param name the name of the character card
     * @param era the era to which the character card belongs
     * @param minNumPlayers the minimum number of players required to use this character card
     * @param type the specific type of the character card
     */
    public CharacterCard(String name, Era era, int minNumPlayers, CharacterType type) {
        super(name, era, minNumPlayers);
        this.type = type;
    }

    /**
     * Gets the specific type of the character card.
     * @return the character card's type.
     */
    public CharacterType getType() {
        return type;
    }

}
