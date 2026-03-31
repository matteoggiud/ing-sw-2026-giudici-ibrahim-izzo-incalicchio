package it.polimi.ingsw.model.deck;

import java.util.ArrayList;

import it.polimi.ingsw.model.card.*;

/**
 * This class represents the building deck, which is a collection of building cards that players can draw from during the game.
 */
public class BuildingDeck {

    /**
     * The building deck is a map that associates each era with a list of building cards. Each era has its own list of building cards, and players can only draw building cards from the current era.
     */
    private final ArrayList<BuildingCard> buildingDeck;

    /**
     * Constructs a new BuildingDeck with an empty list of building cards.
     */
    public BuildingDeck() {
        this.buildingDeck = new ArrayList<BuildingCard>();
    }

    public BuildingDeck(ArrayList<BuildingCard> buildingDeck) {
        this.buildingDeck = new ArrayList<>(buildingDeck);
    }

    public ArrayList<BuildingCard> getBuildingCards() {
        return buildingDeck;
    }


}