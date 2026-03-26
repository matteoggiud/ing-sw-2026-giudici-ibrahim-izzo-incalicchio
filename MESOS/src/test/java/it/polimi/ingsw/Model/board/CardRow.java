package it.polimi.ingsw.Model.board;

/**
 * This class represents the card row of the board, which is composed by a tribe row and a building row.
 */
public class CardRow {

    private TribeRow tribeRow;
    private BuildingRow buildingRow;

    /// Constructor for the card row, which initializes the tribe row and the building row.
    public CardRow() {
        this.tribeRow = new TribeRow();
        this.buildingRow = new BuildingRow();
    }

    /**
     * Getter for the tribe row.
     */
    public TribeRow getTribeRow() {
        return tribeRow;
    }

    /**
     * Getter for the building row;
     */
    public BuildingRow getBuildingRow() {
        return buildingRow;
    }




}
