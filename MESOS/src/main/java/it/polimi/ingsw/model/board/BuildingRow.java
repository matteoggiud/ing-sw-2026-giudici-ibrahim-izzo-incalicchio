package it.polimi.ingsw.model.board;
import it.polimi.ingsw.model.card.BuildingCard;
import it.polimi.ingsw.model.deck.BuildingDeck;

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
     * Adds all building cards from a building deck to the building row.
     *
     * @param buildingDeck the building deck from which the building cards will be added to the building row
     */
    public void add(BuildingDeck buildingDeck) {
        this.buildingCards.addAll(buildingDeck.getBuildingCards());
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
     * Transfers the building cards from other building row to this building row.
     *
     * @param other the building row from which the building cards will be transferred to this building row
     */
    public void transferBuildingCardsFrom(BuildingRow other) {
        this.buildingCards.addAll(other.buildingCards);
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


    /**
     * Checks if the building row is empty.
     *
     * @return true if the building row is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.buildingCards.isEmpty();
    }
}
