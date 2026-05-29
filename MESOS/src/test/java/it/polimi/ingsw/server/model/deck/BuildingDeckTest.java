package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;

import java.util.ArrayList;

class BuildingDeckTest {

    private BuildingDeck buildingDeck;
    private int nextCardId;
    private Era1 era1;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        buildingDeck = new BuildingDeck();
        nextCardId = 1;
        era1 = new Era1();
    }

    /**
     * Tests the retrieval and integrity of the building cards list, ensuring it is initially empty,
     * updates correctly upon addition of cards, and maintains structural properties.
     */
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

        var cards = buildingDeck.getBuildingCards();
        cards.clear();
        org.junit.jupiter.api.Assertions.assertEquals(2, buildingDeck.getBuildingCards().size());
    }

    /**
     * Tests adding a single building card to the deck, verifying the deck's size and contents
     * correctly reflect the addition.
     */
    @org.junit.jupiter.api.Test
    void addBuildingCard() {
        BuildingCard card = newBuilding(4);

        buildingDeck.addBuildingCard(card);

        org.junit.jupiter.api.Assertions.assertEquals(1, buildingDeck.getBuildingCards().size());
        org.junit.jupiter.api.Assertions.assertTrue(buildingDeck.getBuildingCards().contains(card));
    }

    /**
     * Tests adding a list of building cards to the deck, verifying all cards are accurately
     * incorporated into the internal deck structure.
     */
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
        return new BuildingCard(nextCardId++, "Building" + nextCardId, era1, 2, foodCost, 2, effect, "");
    }
}
