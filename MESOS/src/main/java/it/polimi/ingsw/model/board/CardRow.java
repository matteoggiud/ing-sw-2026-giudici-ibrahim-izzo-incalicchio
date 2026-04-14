package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.card.TribeCard;
import it.polimi.ingsw.model.deck.BuildingDeck;

/**
 * This class represents the card row of the board, which is composed by a tribe row and a building row.
 */
public class CardRow {

    /**
     * The tribe section of a card row
     */
    private final TribeRow tribeRow;

    /**
     * The building section of a card row
     */
    private final BuildingRow buildingRow;

    /**
     * Constructs a new CardRow with an empty tribe row and an empty building row.
     */
    public CardRow() {
        this.tribeRow = new TribeRow();
        this.buildingRow = new BuildingRow();
    }

    /**
     * Gets the tribe row of this card row.
     */
    public TribeRow getTribeRow() {
        return tribeRow;
    }

    /**
     * Gets the building row of this card row.
     */
    public BuildingRow getBuildingRow() {
        return buildingRow;
    }


    /**
     * Adds a tribe card to the tribe row of this card row.
     *
     * @param tribeCard the tribe card to be added to the tribe row
     * @throws IllegalArgumentException if the tribeCard is null
     */
    public void addCard(TribeCard tribeCard) throws IllegalArgumentException {
        if(tribeCard == null) throw new IllegalArgumentException("Arguments cannot be null");

        tribeRow.add(tribeCard);
    }

    /**
     * Clears all the cards from the tribe row of this card row.
     */
    public void clearBuildings() {
        buildingRow.clear();
    }

    /**
     * Transfers all the building cards from another card row to this card row.
     *
     * @param other the card row from which to transfer the building cards
     */
    public void transferBuildingRowFrom(CardRow other) {
        buildingRow.transferBuildingCardsFrom(other.getBuildingRow());
    }


    public void addBuildingRowFrom(BuildingDeck buildingDeck) {
        buildingRow.add(buildingDeck);
    }

    public void transferTribeRowFrom(CardRow other) {
        this.tribeRow.transferTribeCardsFrom(other.getTribeRow());
    }

    public void clearRow() {
        tribeRow.clear();
        buildingRow.clear();
    }

    public int countRow() {
        return tribeRow.count() + buildingRow.count();
    }
}
