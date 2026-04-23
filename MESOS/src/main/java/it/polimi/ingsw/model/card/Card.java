package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.deck.PlayerDeck;
import it.polimi.ingsw.model.board.CardRow;
import it.polimi.ingsw.model.core.Player;


import java.util.UUID;

/**
 * Abstract class representing a generic card in the game.
 * This class serves as a base for specific types of cards, such as BuildingCard, CharacterCard, etc.
 *
 */
public abstract class Card {

    /** The unique identifier of the card. */
    private final int cardId;

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
     * @throws IllegalArgumentException if minNumPlayers is less than 2 or greater than 5 or some values are null
     */
    protected Card(int cardId, String name, Era era, int minNumPlayers) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Card name cannot be null.");
        }
        if (era == null) {
            throw new IllegalArgumentException("Card era cannot be null.");
        }
        if (minNumPlayers < 2 || minNumPlayers > 5) {
            throw new IllegalArgumentException("Minimum number of players must be between 2 and 5.");
        }
        this.cardId = cardId;
        this.name = name;
        this.era = era;
        this.minNumPlayers = minNumPlayers;
    }

    protected Card(String name, Era era, int minNumPlayers) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Card name cannot be null.");
        }
        if (era == null) {
            throw new IllegalArgumentException("Card era cannot be null.");
        }
        if (minNumPlayers < 2 || minNumPlayers > 5) {
            throw new IllegalArgumentException("Minimum number of players must be between 2 and 5.");
        }
        this.cardId = -1;
        this.name = name;
        this.era = era;
        this.minNumPlayers = minNumPlayers;
    }

    /**
     * Gets the unique identifier of the card.
     *
     * @return the card's id.
     */
    public int getCardId() {
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


    /**
     * Adds this card to the specified player's deck.
     *
     * @param playerDeck the player's deck to which this card will be added
     */
    public abstract void addTo(PlayerDeck playerDeck);

    public boolean canBeSelectedFrom(CardRow row, Player player) {
        return false;
    }


    public void validateSelectableFrom(CardRow row) {
        throw new IllegalArgumentException("Unsupported card type selected.");
    }

    public void resolveFromOfferRow(Player player, CardRow row) {
        throw new IllegalArgumentException("Unsupported card type selected.");
    }



}

