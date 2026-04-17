package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;

/**
 * Represents a Gatherer Character Card in the game.
 */
public class GathererCharacterCard extends CharacterCard {

    /**
     * Constructs a new GathererCharacterCard with the specified attributes.
     *
     * @param name          the name of the card
     * @param era           the era of the card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @throws IllegalArgumentException if numeric constraints are violated or if something is null
     */
    public GathererCharacterCard(String name, Era era, int minNumPlayers) {
        super(name, era, minNumPlayers, CharacterType.GATHERER);
    }

}
