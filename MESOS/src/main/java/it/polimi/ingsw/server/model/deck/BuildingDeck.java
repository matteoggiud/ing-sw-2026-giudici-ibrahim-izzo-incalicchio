package it.polimi.ingsw.server.model.deck;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.server.model.card.*;
import it.polimi.ingsw.server.model.card.BuildingCard;

/**
 * This class represents the building deck, which is a collection of building cards that players can draw from during the game.
 */
public class BuildingDeck {

    /**
     * The building deck contains some building cards
     */
    private final List<BuildingCard> buildingDeck;

    /**
     * Constructor for the BuildingDeck class, it initializes the building deck with an empty map of eras and building cards.
     */
    public BuildingDeck() {
        this.buildingDeck = new ArrayList<>();
    }

    /**
     * Constructor for the BuildingDeck class, it initializes the building deck with a list of building cards.
     * @param buildingsEra1 the list of building cards to be added to the building deck.
     */
    public BuildingDeck(List<BuildingCard> buildingsEra1) {
        if (buildingsEra1 == null) {
            throw new IllegalArgumentException("Building card list cannot be null");
        }

        this.buildingDeck = new ArrayList<>(buildingsEra1);
    }

    /**
     * Gets the building deck.
     *
     * @return the building deck.
     */
    public List<BuildingCard> getBuildingCards() {
        return new ArrayList<>(buildingDeck);
    }

    /**
     * This method allows to add a building card to the building deck.
     *
     * @param card The building card to be added to the building deck.
     */
    public void addBuildingCard(BuildingCard card) {
        this.buildingDeck.add(card);
    }

    /**
     * This method allows to add a collection of building cards to the building deck.
     *
     * @param cards The collection of building cards to be added to the building deck.
     */
    public void addBuildingCards(List<BuildingCard> cards) {
        this.buildingDeck.addAll(cards);
    }


}
