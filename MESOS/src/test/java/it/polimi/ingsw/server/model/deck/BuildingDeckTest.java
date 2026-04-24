package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.BuildingCard;

import java.util.ArrayList;

class BuildingDeckTest {

    private BuildingDeck buildingDeck;
    private int nextCardId;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        buildingDeck = new BuildingDeck();
        nextCardId = 1;
    }

    @org.junit.jupiter.api.Test
    void getBuildingCards() {
        org.junit.jupiter.api.Assertions.assertTrue(buildingDeck.getBuildingCards().isEmpty());

        BuildingCard card1 = newBuilding(3);
        BuildingCard card2 = newBuilding(5);

        buildingDeck.addBuildingCard(card1);
        buildingDeck.addBuildingCard(card2);

        org.junit.jupiter.api.Assertions.assertEquals(2, buildingDeck.getBuildingCards().size());
        org.junit.jupiter.api.Assertions.assertTrue(buildingDeck.getBuildingCards().contains(card1));
        org.junit.jupiter.api.Assertions.assertTrue(buildingDeck.getBuildingCards().contains(card2));
    }

    @org.junit.jupiter.api.Test
    void addBuildingCard() {
        BuildingCard card = newBuilding(4);

        buildingDeck.addBuildingCard(card);

        org.junit.jupiter.api.Assertions.assertEquals(1, buildingDeck.getBuildingCards().size());
        org.junit.jupiter.api.Assertions.assertTrue(buildingDeck.getBuildingCards().contains(card));
    }

    @org.junit.jupiter.api.Test
    void addBuildingCards() {
        BuildingCard card1 = newBuilding(2);
        BuildingCard card2 = newBuilding(3);
        BuildingCard card3 = newBuilding(4);

        ArrayList<BuildingCard> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);

        buildingDeck.addBuildingCards(cards);

        org.junit.jupiter.api.Assertions.assertEquals(3, buildingDeck.getBuildingCards().size());
        org.junit.jupiter.api.Assertions.assertTrue(buildingDeck.getBuildingCards().contains(card1));
        org.junit.jupiter.api.Assertions.assertTrue(buildingDeck.getBuildingCards().contains(card2));
        org.junit.jupiter.api.Assertions.assertTrue(buildingDeck.getBuildingCards().contains(card3));
    }

    private BuildingCard newBuilding(int foodCost) {
        it.polimi.ingsw.server.model.effect.building.IBuildingEffect effect = new it.polimi.ingsw.server.model.effect.building.IBuildingEffect() {
        };
        return new BuildingCard(nextCardId++, "Building" + nextCardId, it.polimi.ingsw.server.model.card.Era.ERA_1, 2, foodCost, 2, effect);
    }
}