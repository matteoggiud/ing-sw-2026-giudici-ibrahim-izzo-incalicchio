package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;
import it.polimi.ingsw.model.deck.PlayerDeck;

/**
 * Represent a Shaman character card in the game.
 * Shaman cards provide a certain number of shaman stars.
 */
public class ShamanCharacterCard extends CharacterCard {
    /**
     * The number of shaman stars provided by this card.
     */
    private final int shamanStars;

    /**
     * Construct a new ShamanCharacterCard with the specified attributes.
     *
     * @param name          the name of the shaman character card
     * @param era           the era of the shaman character card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @param shamanStars   the number of shaman stars provided by this card (>= 1)
     * @throws IllegalArgumentException if numeric constraints are violated or something is null
     */
    public ShamanCharacterCard(String name, Era era, int minNumPlayers, int shamanStars) {
        super(name, era, minNumPlayers);
        if (shamanStars <= 0)
            throw new IllegalArgumentException("Shaman stars must be non-negative");
        this.shamanStars = shamanStars;
    }


    /**
     * Gets the number of shaman stars provided by this card.
     *
     * @return the number of shaman stars.
     */
    public int getShamanStars() {
        return shamanStars;
    }

    @Override
    public void onAddToPlayerDeck(PlayerDeck playerDeck) {
        playerDeck.incrementTypeCount(CharacterType.SHAMAN);
    }
}
