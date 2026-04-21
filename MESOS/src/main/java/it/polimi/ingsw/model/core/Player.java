package it.polimi.ingsw.model.core;

import it.polimi.ingsw.model.card.BuildingCard;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.IllegalFoodArgumentException;
import it.polimi.ingsw.model.deck.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class represents a player in the game.
 */
public class Player {
    /**
     * The player's id
     */
    private final UUID playerId;

    /**
     * The player's nickname
     */
    private final String nickname;

    /**
     * The player's totem
     */
    private Totem totem;

    /**
     * The player's deck of cards
     */
    private final PlayerDeck playerDeck;

    /**
     * The player's current amount of prestige points
     */
    private int PP;

    /**
     * The player's current amount of food
     */
    private int food;



    /**
     * Constructs a new Player with the specified nickname and totem.
     *
     * @param nickname the player's nickname
     */
    public Player(String nickname) {
        this.playerId = UUID.randomUUID();
        this.nickname = nickname;
        this.totem = null;
        this.playerDeck = new PlayerDeck();
        this.PP = 0;
        this.food = 0;
    }

    /**
     * Gets the player's UUID.
     *
     * @return the player's unique identifier.
     */
    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Gets the player's nickname.
     *
     * @return the player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the player's totem.
     *
     * @return the player's totem.
     */
    public Totem getTotem() {
        return totem;
    }

    /**
     * Gets the player's deck
     *
     * @return the player's deck.
     */
    public PlayerDeck getPlayerDeck() {
        return playerDeck;
    }

    /**
     * Gets the player's current amount of prestige points.
     *
     * @return the player's prestige points.
     */
    public int getPP() {
        return PP;
    }

    /**
     * Gets the player's current amount of food.
     *
     * @return the player's food.
     */
    public int getFood() {
        return food;
    }

    /**
     * Adds the specified amount of food to the player's total.
     * Negative values will decrease the player's food.
     *
     * @param food the amount of food to add (positive) or to decrease (negative).
     */
    public void addFood(int food) {
        this.food += food;
    }

    /**
     * Adds the specified amount of prestige points to the player's total.
     * Negative values will decrease the player's prestige points.
     *
     * @param PP the amount of prestige points to add (positive) or to decrease (negative).
     */
    public void addPP(int PP) {
        this.PP += PP;
    }

    /**
     * Checks if the player can buy the specified building card based on their current food and any applicable discounts from their deck.
     *
     * @param buildingCard the building card the player wants to buy
     * @throws IllegalFoodArgumentException if the player does not have enough food to buy the card after applying discounts.
     */
    public void canBuyBuildingCard(BuildingCard buildingCard) throws IllegalFoodArgumentException {
        int foodDiscount = this.playerDeck.getTotalBuilderDiscount(); // Calculate the total food discount from the player's deck

        if (buildingCard.getFoodCost() - foodDiscount > this.food) { // If the player doesn't have enough food to buy the card
            throw new IllegalFoodArgumentException("Not enough food to buy this building card.");
        }

        playerDeck.addCard(buildingCard); // Add the building card to the player's deck
        this.food -= Math.max((buildingCard.getFoodCost() - foodDiscount), 0); // Deduct the food cost from the player's total, ensuring it doesn't go negative
    }

    /**
     * Adds the specified cards to the player's deck.
     *
     * @param cards the list of cards to be added to the player's deck
     */
    public void addCardsInDeck(ArrayList<Card> cards) {
        for (Card card : cards) {
            playerDeck.addCard(card);
        }

    }

    /**
     * Sets the player's totem.
     *
     * @param totem the totem to be set for the player
     */
    public void setTotem(Totem totem) {
        this.totem = totem;
    }
}
