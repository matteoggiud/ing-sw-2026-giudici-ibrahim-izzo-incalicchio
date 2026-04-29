package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.era.Era;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

/**
 * This class is a wrapper for the building decks, it contains a map that associate every era with the corresponding building deck, it is used to manage the building decks in the game.
 */
public class BuildingDecks {

    /**
     * The building decks is a map that associate every era with the corresponding building deck.
     */
    private final HashMap<Era, BuildingDeck> buildingDecks;

    /**
     * Constructs a new BuildingDecks object, initializing the building decks map as an empty EnumMap of Era to BuildingDeck.
     */
    public BuildingDecks() {
        this.buildingDecks = new HashMap<>();
        for (Era era : Era.values()) {
            buildingDecks.put(era, new BuildingDeck());
        }
    }

    /**
     * This method allows to get the building deck corresponding to a specific era.
     *
     * @param era The era for which to retrieve the building deck.
     * @return the building deck corresponding to the specified era.
     */
    public BuildingDeck getBuildingDeck(Era era) {
        return buildingDecks.get(era);
    }

    /**
     * This method allows to add a building card to the corresponding building deck based on the era of the card.
     *
     * @param cards The collection of building cards to be added to the corresponding building decks.
     * @throws IllegalArgumentException if the cards parameter is null.
     */
    public void addBuildingCards(List<BuildingCard> cards) throws IllegalArgumentException {
        if (cards == null) {
            throw new IllegalArgumentException("Cards cannot be null");
        }

        for (BuildingCard card : cards) {
            buildingDecks.get(card.getEra()).addBuildingCard(card);
        }
    }

    /**
     * This method allows to set the building decks with a new composition of building cards
     * @param deckComposition the new composition of building cards to be set in the building decks
     * @throws IllegalArgumentException if the deckComposition parameter is null or empty
     */
    public void setBuildingDeck(List<BuildingCard> deckComposition) {
        if (deckComposition == null || deckComposition.isEmpty()) {
            throw new IllegalArgumentException("Cards cannot be null or empty");
        }

        this.buildingDecks.clear();
        for (Era era : Era.values()) {
            buildingDecks.put(era, new BuildingDeck());
        }

        for (BuildingCard card : deckComposition) {
            buildingDecks.get(card.getEra()).addBuildingCard(card);
        }
    }
}
