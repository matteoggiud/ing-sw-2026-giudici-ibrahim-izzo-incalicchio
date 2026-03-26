package it.polimi.ingsw.Model.Board;
import it.polimi.ingsw.Model.Card.BuildingCard;
import it.polimi.ingsw.Model.Card.TribeCard;

import java.util.List;
import java.util.ArrayList;

public class BuildingRow {
    private List<BuildingCard> buildingCards = new ArrayList<>();


    public void add(BuildingCard card) {
        this.buildingCards.add(card);
    }

    public void remove(BuildingCard card) {
        this.buildingCards.remove(card);
    }

    public List<BuildingCard> getBuildingCards() {
        List<BuildingCard> buildings = new ArrayList<>();
        for (int i = 0; i < buildingCards.size(); i++) {
            buildings.add(buildingCards.get(i));
        }
        return buildings;
    }

    public void transferBuildingCardsTo(BuildingRow other) {
        for (int i = 0; i < this.buildingCards.size(); i++) {
            other.add(this.buildingCards.get(i));
        }
        this.buildingCards.clear();
    }

}
