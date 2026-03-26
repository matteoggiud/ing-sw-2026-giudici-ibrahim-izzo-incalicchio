package it.polimi.ingsw.Model.Card;

/**
 * This class represents a character card, which is a type of card in the game.
 * It extends the Card class and adds a specific type for character cards.
 */
public class CharacterCard extends Card {
    private final CharacterType type;

    /**
     * Constructor for the CharacterCard class. It initializes the card with a name, era, minimum number of players, and character type.
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
     * Getter for the type of the character card.
     * @return the type of the character card
     */
    public CharacterType getType() {
        return type;
    }

}
