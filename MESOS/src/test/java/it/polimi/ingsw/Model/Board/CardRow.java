package it.polimi.ingsw.Model.Board;
import java.util.List;

public class CardRow {

    private TribeRow tribeRow;
    private BuildingRow buildingRow;


    public CardRow() {
        this.tribeRow = new TribeRow();
        this.buildingRow = new BuildingRow();
    }

    public TribeRow getTribeRow() {
        return tribeRow;
    }

    public BuildingRow getBuildingRow() {
        return buildingRow;
    }




}
