package it.polimi.ingsw.server.model.card.characters;

import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.card.Era;
import it.polimi.ingsw.server.model.deck.PlayerDeck;

/**
 * Represents an Artist Character Card in the game.
 */
public class ArtistCharacterCard extends CharacterCard {

    /**
     * Constructs a new ArtistCharacterCard with the specified attributes.
     *
     * @param cardId        the unique identifier of the card
     * @param name          the name of the card
     * @param era           the era of the card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @param imageKey      the key used to retrieve the card's image from the resource manager
     * @throws IllegalArgumentException if numeric constraints are violated or if something is null
     */
    public ArtistCharacterCard(int cardId, String name, Era era, int minNumPlayers, String imageKey) throws IllegalArgumentException {
        super(cardId, name, era, minNumPlayers, imageKey);
    }

    /**
     * @param playerDeck the player's deck to which this card is being added.
     */
    @Override
    public void onAddToPlayerDeck(PlayerDeck playerDeck) {
        playerDeck.incrementTypeCount(CharacterType.ARTIST);
    }

}
