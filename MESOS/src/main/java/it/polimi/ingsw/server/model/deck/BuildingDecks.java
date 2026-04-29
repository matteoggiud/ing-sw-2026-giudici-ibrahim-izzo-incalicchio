package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.era.Era2;
import it.polimi.ingsw.server.model.era.Era3;

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
    private HashMap<Era, BuildingDeck> buildingDecks;

    /**
     * Constructs a new BuildingDecks object, initializing the building decks map as an empty EnumMap of Era to BuildingDeck.
     */
    public BuildingDecks() {
        this.buildingDecks = new HashMap<>();
        Era era1 = new Era1();
        Era era2 = new Era2();
        Era era3 = new Era3();
        buildingDecks = era1.createBuildingDeck(buildingDecks);
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

        Era era1 = new Era1();
        Era era2 = new Era2();
        Era era3 = new Era3();

        this.buildingDecks.clear();
        this.buildingDecks = era1.createBuildingDeck(this.buildingDecks);
        this.buildingDecks = era2.createBuildingDeck(this.buildingDecks);
        this.buildingDecks = era3.createBuildingDeck(this.buildingDecks);

        for (BuildingCard card : deckComposition) {
            buildingDecks.get(card.getEra()).addBuildingCard(card);
        }
    }
}
