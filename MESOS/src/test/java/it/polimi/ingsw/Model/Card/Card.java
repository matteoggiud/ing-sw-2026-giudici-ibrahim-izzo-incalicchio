package it.polimi.ingsw.Model.Card;

import java.util.UUID;

/**
 * Abstract class representing a generic card in the game.
 * This class serves as a base for specific types of cards, such as BuildingCard, CharacterCard, etc.
 *
 */
public abstract class Card {

    /** The unique identifier of the card. */
    private final UUID cardId;

    /** The display name of the card. */
    private final String name;

    /** The era to which the card belongs (Era I, II, III) */
    private final Era era;

    /** The minimum number of players required for this card to be included in the game. */
    private final int minNumPlayers;

    /**
     * Construct a new Card with specific properties.
     *
     * @param name the name of the building card
     * @param era the era of the building card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     */
    public Card(String name, Era era, int minNumPlayers) {
        this.cardId = UUID.randomUUID();
        this.name = name;
        this.era = era;
        this.minNumPlayers = minNumPlayers;
    }

    /**
     * Gets the unique identifier of the card.
     *
     * @return the card's UUID.
     */
    public UUID getCardId() {
        return this.cardId;
    }

    /**
     * Gets the display name of the card.
     *
     * @return the card name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the era to which the card belongs.
     *
     * @return the era of the card.
     */
    public Era getEra() {
        return era;
    }

    /**
     * Gets the minimum number of players required to include this card in game.
     * @return the minimum player count.
     */
    public int getMinNumPlayers() {
        return minNumPlayers;
    }

    /**
     * Checks if the card can be included in a game with the specified number of players.
     *
     * @param countPlayers the current number of players in the game.
     * @return true if the card is available for the given number of players, false otherwise
     */
    public boolean isAvailableForPlayerCount(int countPlayers){
        return countPlayers >= minNumPlayers;
    }
}

