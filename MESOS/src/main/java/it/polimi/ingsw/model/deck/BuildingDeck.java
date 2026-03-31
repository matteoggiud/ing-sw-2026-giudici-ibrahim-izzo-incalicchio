package it.polimi.ingsw.model.deck;

import java.util.HashMap;
import java.util.LinkedList;

import it.polimi.ingsw.model.card.*;

/**
 * This class represents the building deck, which is a collection of building cards that players can draw from during the game.
 */
public class BuildingDeck {

    /**
     * The building deck is a map that associates each era with a list of building cards. Each era has its own list of building cards, and players can only draw building cards from the current era.
     */
    private final HashMap<Era, LinkedList<BuildingCard>> buildingDeck;

    /**
     * The current era of the game, which determines which building cards are available for players to draw.
     */
    private Era currentEra;

    /**
     * Constructor for the BuildingDeck class, it initializes the building deck with an empty map of eras and building cards.
     */
    public BuildingDeck() {
        this.buildingDeck = new HashMap<>();
        for(Era era : Era.values()){
            this.buildingDeck.put(era, new LinkedList<>());
        }
        this.currentEra = Era.ERA_1;
    }

    /**
     * This method allows a player to draw a building card from the building deck.
     * The method checks the current era and draws a card from the corresponding list of building cards.
     * @return Building Card drawn
     */
    public BuildingCard draw() {
        while(buildingDeck.get(currentEra).isEmpty() && currentEra.ordinal() < Era.ERA_3.ordinal()){
            this.currentEra = Era.values()[this.currentEra.ordinal() + 1];
        }
        return buildingDeck.get(currentEra).removeFirst();
    }
}