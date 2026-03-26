package it.polimi.ingsw.Model.board;
import it.polimi.ingsw.Model.Card.BuildingCard;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the building row of the board, which is composed by a list of building cards.
 * The building row has methods to add and remove building cards,
 * to get the list of building cards, and to transfer the building cards to another building row.
 */
public class BuildingRow {
    private List<BuildingCard> buildingCards = new ArrayList<>();

    /**
     * Adds a building card to the building row.
     * @param card the building card to be added to the building row
     */
    public void add(BuildingCard card) {
        this.buildingCards.add(card);
    }

    /**
     * Removes a building card from the building row.
     * @param card the building card to be removed from the building row
     */
    public void remove(BuildingCard card) {
        this.buildingCards.remove(card);
    }

    /**
     * Gets the list of building cards in the building row.
     * @return the list of building cards in the building row
     */
    public List<BuildingCard> getBuildingCards() {
        List<BuildingCard> buildings = new ArrayList<>();
        for (int i = 0; i < buildingCards.size(); i++) {
            buildings.add(buildingCards.get(i));
        }
        return buildings;
    }

    /**
     * Transfers the building cards from this building row to another building row.
     * @param other the building row to which the building cards will be transferred
     */
    public void transferBuildingCardsTo(BuildingRow other) {
        for (int i = 0; i < this.buildingCards.size(); i++) {
            other.add(this.buildingCards.get(i));
        }
        this.buildingCards.clear();
    }

}
