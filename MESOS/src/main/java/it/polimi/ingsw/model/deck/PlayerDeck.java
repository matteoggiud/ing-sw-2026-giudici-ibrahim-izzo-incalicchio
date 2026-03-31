package it.polimi.ingsw.model.deck;

import java.util.ArrayList;
import java.util.LinkedList;

import it.polimi.ingsw.model.card.*;

/**
 * This class represents the player's deck,
 * which is a collection of character and building cards that a player has in their possession during the game.
 */
public class PlayerDeck {

    /**
     * The list of character cards that the player has in their deck
     */
    private ArrayList<CharacterCard> characters;

    /**
     * The list of building cards that the player has in their deck
     */
    private ArrayList<BuildingCard> buildings;

    /**
     * The total builder discount that the player has accumulated from their character cards.
     */
    private int totalBuilderDiscount;

    /**
     * The total gatherer discount that the player has accumulated from their character cards.
     */
    private int totalGathererDiscount;

    /**
     * The total number of shaman stars that the player has accumulated from their character cards.
     */
    private int totalShamanStars;

    /**
     * The total number of different inventor items that the player has accumulated from their character cards.
     */
    private int differentInventorItem;

    /**
     * The total number of card that have the character type HUNTER
     */
    private int totalHunter;

    /**
     * The total number of card that have the character type ARTIST
     */
    private int totalArtist;

    /**
     * Constructor for the PlayerDeck class,
     * it initializes the player's deck with empty lists of character and building cards,
     * and sets all discounts and counts to zero.
     */
    public PlayerDeck(){
        this.characters = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.totalBuilderDiscount = 0;
        this.totalGathererDiscount = 0;
        this.totalShamanStars = 0;
        this.differentInventorItem = 0;
        this.totalHunter = 0;
        this.totalArtist = 0;
    }

    /**
     * This method counts the number of character cards in the player's deck that have a specific character type.
     * @param type The type we want to count
     * @return the number of character cards in the player's deck that have the specified character type.
     */
    public int getCountByType(CharacterType type) {
        int count = 0;
        for (CharacterCard card : this.characters) {
            if (card.getType() == type) {
                count++;
            }
        }
        return count;
    }

    /**
     * This method adds a character card to the player's deck
     * @param card to be added
     */
    public void addCard(CharacterCard card){
        this.characters.add(card);
    }

    /**
     * This method adds a building card to the player's deck
     * @param card to be added
     */
    public void addCard(BuildingCard card){
        this.buildings.add(card);
    }

    /**
     * This method returns the list of character cards in the player's deck.
     * @return All the character card of the player
     */
    public ArrayList<CharacterCard> getCharacters(){
        return this.characters;
    }

    /**
     * This method returns the list of building cards in the player's deck.
     * @return All the building card of the player
     */
    public ArrayList<BuildingCard> getBuildings() {
        return this.buildings;
    }
}