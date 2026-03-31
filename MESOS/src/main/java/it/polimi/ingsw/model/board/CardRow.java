package it.polimi.ingsw.model.board;

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
     * Constructs a new CardRow with the given tribe row and building row.
     *
     * @param tribeRow The tribe row to be used in this card row
     * @param buildingRow The building row to be used in this card row
     * @throws IllegalArgumentException if either tribeRow or buildingRow is null
     */
    public CardRow(TribeRow tribeRow, BuildingRow buildingRow) throws IllegalArgumentException {
        if (tribeRow == null || buildingRow == null) throw new IllegalArgumentException("Arguments cannot be null");

        this.tribeRow = tribeRow;
        this.buildingRow = buildingRow;
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


}
