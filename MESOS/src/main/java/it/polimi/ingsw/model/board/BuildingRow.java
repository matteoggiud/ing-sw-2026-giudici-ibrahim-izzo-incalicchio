package it.polimi.ingsw.model.board;
import it.polimi.ingsw.model.card.BuildingCard;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the building row of the board.
 */
public class BuildingRow {
    /** List of building cards in the building row. */
    private final List<BuildingCard> buildingCards = new ArrayList<>();

    /**
     * Adds a building card to the building row.
     *
     * @param card the building card to be added to the building row
     */
    public void add(BuildingCard card) {
        this.buildingCards.add(card);
    }

    /**
     * Removes a building card from the building row.
     *
     * @param card the building card to be removed from the building row
     */
    public void remove(BuildingCard card) {
        this.buildingCards.remove(card);
    }

    /**
     * Clears all building cards from the building row.
     */
    public void clear() {
        this.buildingCards.clear();
    }

    /**
     * Gets the list of building cards in the building row.
     *
     * @return the list of building cards in the building row
     */
    public List<BuildingCard> getBuildingCards() {
        return new ArrayList<>(this.buildingCards);
    }

    /**
     * Transfers the building cards from this building row to another building row.
     *
     * @param other the building row to which the building cards will be transferred
     */
    public void transferBuildingCardsTo(BuildingRow other) {
        other.buildingCards.addAll(this.buildingCards);
        this.clear();
    }

    /**
     * Copies the building cards from this building row to another building row.
     *
     * @param other the building row to which the building cards will be copied
     */
    public void copyBuildingCardsTo(BuildingRow other) {
        other.buildingCards.addAll(this.buildingCards);
    }

}
