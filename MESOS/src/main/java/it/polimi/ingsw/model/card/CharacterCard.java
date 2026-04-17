package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.characters.InventorType;
import it.polimi.ingsw.model.deck.PlayerDeck;

import java.util.Optional;

/**
 * This class represents a character card, which is a type of card in the game.
 */
public class CharacterCard extends TribeCard {

    public boolean isHunter() { return false; }
    public boolean isArtist() { return false; }
    public boolean isGatherer() { return false; }
    public boolean isBuilder() { return false; }
    public boolean isInventor() { return false; }
    public boolean isShaman() { return false; }

    /**
     * Construct a new CharacterCard with specific properties.
     * @param name the name of the character card
     * @param era the era to which the character card belongs
     * @param minNumPlayers the minimum number of players required to use this character card
     * @throws IllegalArgumentException if numeric constraints are violated or something is null.
     */
    public CharacterCard(String name, Era era, int minNumPlayers) throws IllegalArgumentException {
        super(name, era, minNumPlayers);
    }

    public int getBuilderFoodDiscount() { return 0; }
    public int getShamanStars() { return 0; }
    public Optional<InventorType> getInventorType() { return Optional.empty(); }
    public int getEndGamePPBonus() { return 0; }
    public boolean hasFoodIcon() { return false; }



    /**
     * Adds this card to the specified player's deck.
     *
     * @param playerDeck the player's deck to which this card will be added
     */
    @Override
    public void addTo(PlayerDeck playerDeck) {
        playerDeck.addCharacterCard(this);
    }

}
