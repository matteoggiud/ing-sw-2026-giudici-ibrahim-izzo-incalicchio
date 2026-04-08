package it.polimi.ingsw.model.deck;

import java.util.ArrayList;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.effect.EffectBuildingResult;


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
    public PlayerDeck() {
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
     *
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
     * This method adds a card to the player's deck
     *
     * @param card to be added
     */
    public void addCard(Card card) {
        if (card instanceof BuildingCard) {
            this.buildings.add((BuildingCard) card);
        } else if (card instanceof CharacterCard) {
            this.characters.add((CharacterCard) card);
        } else {
            throw new IllegalArgumentException("Card must be either a BuildingCard or a CharacterCard");
        }
    }


    /**
     * This method returns the list of character cards in the player's deck.
     *
     * @return All the character card of the player
     */
    public ArrayList<CharacterCard> getCharacters() {
        return this.characters;
    }

    /**
     * This method returns the list of building cards in the player's deck.
     *
     * @return All the building card of the player
     */
    public ArrayList<BuildingCard> getBuildings() {
        return this.buildings;
    }

    /**
     * Gets the total builder discount that the player has accumulated from their character cards.
     *
     * @return the total builder discount for the player.
     */
    public int getTotalBuilderDiscount() {
        return totalBuilderDiscount;
    }

    /**
     * Gets the total gatherer discount that the player has accumulated from their character cards.
     *
     * @return the total gatherer discount for the player.
     */
    public int getTotalGathererDiscount() {
        return totalGathererDiscount;
    }

    /**
     * Gets the total number of shaman stars that the player has accumulated from their character cards.
     *
     * @return the total number of shaman stars for the player.
     */
    public int getTotalShamanStars() {
        return totalShamanStars;
    }

    /**
     * Gets the total number of different inventor items that the player has accumulated from their character cards.
     *
     * @return the total number of different inventor items for the player.
     */
    public int getDifferentInventorItem() {
        return differentInventorItem;
    }

    /**
     * Gets the total number of card that have the character type HUNTER
     *
     * @return the total number of card that have the character type HUNTER
     */
    public int getTotalHunter() {
        return totalHunter;
    }

    /**
     * Gets the total number of card that have the character type ARTIST
     *
     * @return the total number of card that have the character type ARTIST
     */
    public int getTotalArtist() {
        return totalArtist;
    }

    /**
     * This method calculates the total discount that the player has for a specific event type, based on the character and building cards in their deck.
     *
     * @param eventEffectType the type of event for which the discount is being calculated
     * @return an EventDiscount object that contains the total discount for the specified event type, including both food and PP discounts.
     */
    public EventDiscount calculateTotalDiscount(EventType eventEffectType) {
        EventDiscount eventDiscount = new EventDiscount(0, 0);
        if (eventEffectType == EventType.SUSTENANCE) {
            eventDiscount.addFood((int) this.characters.stream().filter(c -> c.getType() == CharacterType.GATHERER).count() * 3);
        }

        for (BuildingCard card : this.buildings) {
            if (card.getEffect().getEventEffectType() == eventEffectType) {
                int n_subject = (int) this.characters.stream().filter(c -> c.getType() == card.getEffect().getSubjectType()).count();
                EffectBuildingResult buildingResult = card.getEffect().ApplyEffect(n_subject);
                eventDiscount.addFood(buildingResult.getFoodDelta());
                eventDiscount.addFood(buildingResult.getPpDelta());
            }
        }

        return eventDiscount;
    }
}