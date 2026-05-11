package it.polimi.ingsw.server.model.deck;

import java.util.*;

import it.polimi.ingsw.server.model.card.*;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.effect.building.endgame.IEndGameScoring;
import it.polimi.ingsw.server.model.effect.building.extraction.IExtraActionEffect;
import it.polimi.ingsw.server.model.effect.building.modifiers.IEventModifier;
import it.polimi.ingsw.server.model.effect.building.shamanicstars.IShamanicStarsProvider;
import it.polimi.ingsw.server.model.effect.building.triggers.IEventTrigger;


/**
 * This class represents the player's deck,
 * which is a collection of character and building cards that a player has in their possession during the game.
 */
public class PlayerDeck {

    /**
     * The list of character cards that the player has in their deck
     */
    private final ArrayList<CharacterCard> characters;

    /**
     * The list of building cards that the player has in their deck
     */
    private final ArrayList<BuildingCard> buildings;

    /**
     * A map that keeps track of the count of character cards of each character type in the player's deck.
     */
    private final Map<CharacterType, Integer> characterTypeCounts;

    /**
     * Constructor for the PlayerDeck class,
     * it initializes the player's deck with empty lists of character and building cards,
     * and sets all discounts and counts to zero.
     */
    public PlayerDeck() {
        this.characters = new ArrayList<>();
        this.buildings = new ArrayList<>();

        this.characterTypeCounts = new EnumMap<>(CharacterType.class);
        for (CharacterType characterType : CharacterType.values()) {
            characterTypeCounts.put(characterType, 0);
        }
    }

    public ArrayList<CharacterCard> getCharacters() {
        return characters;
    }

    public ArrayList<BuildingCard> getBuildings() {
        return buildings;
    }

    /**
     * This method counts the number of character cards in the player's deck that have a specific character type.
     *
     * @param type The type we want to count
     * @return the number of character cards in the player's deck that have the specified character type.
     */
    public int countCharactersByType(CharacterType type) {
        return characterTypeCounts.get(type);
    }

    /**
     * This method increments the count of character cards of a specific character type in the player's deck.
     *
     * @param characterType the character type for which the count should be incremented
     */
    public void incrementTypeCount(CharacterType characterType) {
        this.characterTypeCounts.put(characterType, this.characterTypeCounts.get(characterType) + 1);
    }

    /**
     * This method adds a building card to the player's deck
     *
     * @param buildingCard the building card to be added to the player's deck
     */
    public void addBuildingCard(BuildingCard buildingCard) {
        buildings.add(buildingCard);
    }

    /**
     * This method adds a character card to the player's deck
     *
     * @param characterCard the character card to be added to the player's deck
     */
    public void addCharacterCard(CharacterCard characterCard) {
        characters.add(characterCard);
        characterCard.onAddToPlayerDeck(this);
    }

    /**
     * This method adds a card to the player's deck
     *
     * @param card the card to be added to the player's deck
     */
    public void addCard(Card card) {
        card.addTo(this);
    }


    /**
     * Gets the total builder discount that the player has accumulated from their character cards.
     *
     * @return the total builder discount for the player.
     */
    public int getTotalBuilderDiscount() {
        return this.characters.stream()
                .mapToInt(CharacterCard::getBuilderFoodDiscount)
                .sum();
    }

    /**
     * Gets the total gatherer discount that the player has accumulated from their character cards.
     *
     * @return the total gatherer discount for the player.
     */
    public int getTotalGathererDiscount() {
        return countCharactersByType(CharacterType.GATHERER) * 3;
    }

    /**
     * Gets the total number of shaman stars that the player has accumulated from their character cards.
     *
     * @return the total number of shaman stars for the player.
     */
    public int getTotalShamanStars() {
        // Calculate the total number of shaman stars from the character cards in the player's deck
        int totalStars = this.characters.stream()
                .mapToInt(CharacterCard::getShamanStars)
                .sum();

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
        return (int) this.characters.stream()
                .map(CharacterCard::getInventorType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .count();
    }


    /**
     * This method retrieves a list of active event modifiers from the player's building cards.
     */
    public List<IEventModifier> getActiveBuildingEventModifiers() {
        return this.buildings.stream()
                .map(building -> building.getEffect().getEventModifier())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    /**
     * This method retrieves a list of active event triggers from the player's building cards.
     *
     * @return a list of active event triggers from the player's building cards.
     */
    public List<IEventTrigger> getActiveBuildingEventTriggers() {
        List<IEventTrigger> activeTriggers = new ArrayList<>();

        for (BuildingCard building : this.buildings) {
            Optional<IEventTrigger> buildingTriggerOpt = building.getEffect().getEventTrigger();

            // Check if the building has an event trigger and add it to the list of active triggers
            buildingTriggerOpt.ifPresent(activeTriggers::add);
        }

        return activeTriggers;
    }

    public List<IEndGameScoring> getActiveEndGameBuildingScorings() {
        List<IEndGameScoring> endGameScorings = new ArrayList<>();

        for (BuildingCard building : this.buildings) {
            Optional<IEndGameScoring> endGameScoringOpt = building.getEffect().getEndGameScoring();

            // Check if the building has an end game scoring and add it to the list of active end game scorings
            endGameScoringOpt.ifPresent(endGameScorings::add);
        }

        return endGameScorings;
    }

    public List<IExtraActionEffect> getActiveExtractionEffects() {
        List<IExtraActionEffect> extraActionEffects = new ArrayList<>();

        for (BuildingCard building : this.buildings) {
            Optional<IExtraActionEffect> extraActionEffectOpt = building.getEffect().getExtraActionEffect();

            extraActionEffectOpt.ifPresent(extraActionEffects::add);
        }

        return extraActionEffects;
    }


    public void reset() {
        this.characters.clear();
        this.buildings.clear();
        for (CharacterType characterType : CharacterType.values()) {
            characterTypeCounts.put(characterType, 0);
        }
    }
}

