package it.polimi.ingsw.model.deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.characters.ShamanCharacterCard;
import it.polimi.ingsw.model.effect.building.endgame.IEndGameScoring;
import it.polimi.ingsw.model.effect.building.modifiers.IEventModifier;
import it.polimi.ingsw.model.effect.building.shamanicstars.IShamanicStarsProvider;
import it.polimi.ingsw.model.effect.building.triggers.IEventTrigger;


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
    public int countCharactersByType(CharacterType type) {
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
        int totalStars = 0;

        // Calculate the total number of shaman stars from the character cards in the player's deck
        for (CharacterCard card : this.characters) {
            if (card instanceof ShamanCharacterCard shamanCard) {
                totalStars += shamanCard.getShamanStars();
            }
        }

        // Calculate the total number of shaman stars from the building cards in the player's deck
        for (BuildingCard building : this.buildings) {
            Optional<IShamanicStarsProvider> buildingShamanicStarsProviderOpt = building.getEffect().getShamanicStarsProvider();

            if (buildingShamanicStarsProviderOpt.isPresent())
                totalStars += buildingShamanicStarsProviderOpt.get().getBonusShamanStars();
        }
        return totalStars;
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
     * This method retrieves a list of active event modifiers from the player's building cards.
     *
     * @return a list of IEventModifier objects representing the active event modifiers from the player's building cards.
     */
    public List<IEventModifier> getActiveBuildingEventModifiers(EventType eventType) {
        List<IEventModifier> activeModifiers = new ArrayList<>();


        // Gets the active event modifiers from the building cards in the player's deck and add them to the list of active modifiers
        for (BuildingCard building : this.buildings) {
            Optional<IEventModifier> buildingModifierOpt = building.getEffect().getEventModifier();

            // Check if the building has an event modifier and add it to the list of active modifiers
            if (buildingModifierOpt.isPresent() && buildingModifierOpt.get().isSupported(eventType)) {
                activeModifiers.add(buildingModifierOpt.get());
            }
        }

        return activeModifiers;
    }

    public List<IEventTrigger> getActiveBuildingEventTriggers(List<CharacterCard> addedCards) {
        List<IEventTrigger> activeTriggers = new ArrayList<>();

        for (BuildingCard building : this.buildings) {
            Optional<IEventTrigger> buildingTriggerOpt = building.getEffect().getEventTrigger();

            // Check if the building has an event trigger and add it to the list of active triggers
            buildingTriggerOpt.ifPresent(activeTriggers::add);
        }

        return activeTriggers;
    }

    public List<IEndGameScoring> getActiveEndGameBuildingScorings(){
        List<IEndGameScoring> endGameScorings = new ArrayList<>();

        for (BuildingCard building : this.buildings) {
            Optional<IEndGameScoring> endGameScoringOpt = building.getEffect().getEndGameScoring();

            // Check if the building has an end game scoring and add it to the list of active end game scorings
            endGameScoringOpt.ifPresent(endGameScorings::add);
        }

        return endGameScorings;
    }

    public List<CharacterCard> getCharactersByType(CharacterType type) {
        List<CharacterCard> characters = new ArrayList<>();
        for (CharacterCard card : this.characters) {
            if (card.getType() == type) {
                characters.add(card);
            }
        }
        return characters;
    }

}

