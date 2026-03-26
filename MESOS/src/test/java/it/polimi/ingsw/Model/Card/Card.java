package it.polimi.ingsw.Model.Card;

import java.util.UUID;

/**
 * Abstract class that represents a card in the game.
 * It contains common properties and methods that are shared by all types of cards, such as the card's name, era,
 * minimum number of players required to use the card, and a unique identifier for each card instance.
 *
 */
public abstract class Card {
    private final UUID cardId;
    private final String name;
    private final Era era;
    private final int minNumPlayers;

    /**
     * Constructor of the abstract class Card
     * @param name the name of the card for the view
     * @param era the era of the card. Must be an Era enum type
     * @param minNumPlayers the minimum number of players required to use the card. Must be a positive integer and
     */
    public Card(String name, Era era, int minNumPlayers) {
        this.cardId = UUID.randomUUID();
        this.name = name;
        this.era = era;
        this.minNumPlayers = minNumPlayers;
    }

    /**
     * Getter for the id of the card, which is a unique identifier generated using UUID.randomUUID() in the constructor.
     * This id can be used to distinguish between different instances of cards, even if they have the same name and properties.
     * @return the unique identifier of the card as a UUID object
     */
    public UUID getCardId() {
        return this.cardId;
    }

    /**
     * Getter for the name of the card, which is a string that represents the name of the card for the view.
     * This name can be used to display the card's name in the user interface and to identify the card in a human-readable way.
     * @return the name of the card as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the era of the card, which is an enum type that represents the era to which the card belongs.
     * @return the era of the card as an Era enum value
     */
    public Era getEra() {
        return era;
    }

    /**
     * Getter for the minimum number of players required to use the card, which is an integer that represents the minimum number of players needed to include this card in the game.
     * @return the minimum number of players required to use the card as an integer
     */
    public int getMinNumPlayers() {
        return minNumPlayers;
    }

    /**
     * Method that checks if the card is available for a given number of players. It returns true if the number of players is greater than or equal to the minimum number of players required by the card, and false otherwise.
     * This method can be used to determine if a card can be included in the game based on the number of players participating.
     * @param countPlayers the number of players in the game as an integer
     * @return true if the card is available for the given number of players, false otherwise
     */
    public boolean isAvailableForPlayerCount(int countPlayers){
        return countPlayers >= minNumPlayers;
    }
}

