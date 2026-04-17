package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
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
        super(name, era, minNumPlayers);
    }

    /**
     * Indicates that this card is a Gatherer character card.
     *
     * @return true, since this is a Gatherer character card.
     */
    @Override
    public boolean isGatherer() {
        return true;
    }
}
