package it.polimi.ingsw.model.board;

import it.polimi.ingsw.server.model.board.BuildingRow;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.Era;
import it.polimi.ingsw.server.model.deck.BuildingDeck;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildingRowTest {

    private BuildingRow buildingRow;
    private int nextCardId;

    @BeforeEach
    void setUp() {
        buildingRow = new BuildingRow();
        nextCardId = 1;
    }

    @Test
    void add() {
        assertTrue(buildingRow.isEmpty());
        
        BuildingCard card1 = newBuilding(3);
        buildingRow.add(card1);
        
        assertEquals(1, buildingRow.count());
        assertFalse(buildingRow.isEmpty());
        assertTrue(buildingRow.getBuildingCards().contains(card1));
        
        BuildingCard card2 = newBuilding(5);
        buildingRow.add(card2);
        
        assertEquals(2, buildingRow.count());
        assertTrue(buildingRow.getBuildingCards().contains(card2));
    }

    @Test
    void testAdd() {
        BuildingDeck deck = new BuildingDeck();
        BuildingCard card1 = newBuilding(3);
        BuildingCard card2 = newBuilding(4);
        BuildingCard card3 = newBuilding(2);
        
        deck.addBuildingCard(card1);
        deck.addBuildingCard(card2);
        deck.addBuildingCard(card3);
        
        assertTrue(buildingRow.isEmpty());
        
        buildingRow.add(deck);
        
        assertEquals(3, buildingRow.count());
        assertTrue(buildingRow.getBuildingCards().contains(card1));
        assertTrue(buildingRow.getBuildingCards().contains(card2));
        assertTrue(buildingRow.getBuildingCards().contains(card3));
    }

    @Test
    void remove() {
        BuildingCard card1 = newBuilding(3);
        BuildingCard card2 = newBuilding(5);
        BuildingCard card3 = newBuilding(2);
        
        buildingRow.add(card1);
        buildingRow.add(card2);
        buildingRow.add(card3);
        
        assertEquals(3, buildingRow.count());
        
        buildingRow.remove(card2);
        
        assertEquals(2, buildingRow.count());
        assertTrue(buildingRow.getBuildingCards().contains(card1));
        assertFalse(buildingRow.getBuildingCards().contains(card2));
        assertTrue(buildingRow.getBuildingCards().contains(card3));
        
        buildingRow.remove(card1);
        
        assertEquals(1, buildingRow.count());
        assertTrue(buildingRow.getBuildingCards().contains(card3));
    }

    @Test
    void clear() {
        buildingRow.add(newBuilding(3));
        buildingRow.add(newBuilding(5));
        buildingRow.add(newBuilding(2));
        
        assertEquals(3, buildingRow.count());
        
        buildingRow.clear();
        
        assertTrue(buildingRow.isEmpty());
        assertEquals(0, buildingRow.count());
    }

    @Test
    void getBuildingCards() {
        assertTrue(buildingRow.getBuildingCards().isEmpty());
        
        BuildingCard card1 = newBuilding(3);
        BuildingCard card2 = newBuilding(5);
        
        buildingRow.add(card1);
        buildingRow.add(card2);
        
        List<BuildingCard> cards = buildingRow.getBuildingCards();
        assertEquals(2, cards.size());
        assertTrue(cards.contains(card1));
        assertTrue(cards.contains(card2));
        
        // Verify that it returns a copy, not the original list
        cards.clear();
        assertEquals(2, buildingRow.count());
    }

    @Test
    void transferBuildingCardsFrom() {
        BuildingRow sourceRow = new BuildingRow();
        BuildingCard card1 = newBuilding(3);
        BuildingCard card2 = newBuilding(5);
        BuildingCard card3 = newBuilding(2);
        
        sourceRow.add(card1);
        sourceRow.add(card2);
        buildingRow.add(card3);
        
        assertEquals(2, sourceRow.count());
        assertEquals(1, buildingRow.count());
        
        buildingRow.transferBuildingCardsFrom(sourceRow);
        
        assertEquals(3, buildingRow.count());
        assertTrue(buildingRow.getBuildingCards().contains(card1));
        assertTrue(buildingRow.getBuildingCards().contains(card2));
        assertTrue(buildingRow.getBuildingCards().contains(card3));
        assertTrue(sourceRow.isEmpty());
        assertEquals(0, sourceRow.count());
    }

    @Test
    void copyBuildingCardsTo() {
        BuildingRow targetRow = new BuildingRow();
        BuildingCard card1 = newBuilding(3);
        BuildingCard card2 = newBuilding(5);
        BuildingCard card3 = newBuilding(2);
        
        buildingRow.add(card1);
        buildingRow.add(card2);
        targetRow.add(card3);
        
        assertEquals(2, buildingRow.count());
        assertEquals(1, targetRow.count());
        
        buildingRow.copyBuildingCardsTo(targetRow);
        
        assertEquals(2, buildingRow.count());
        assertEquals(3, targetRow.count());
        assertTrue(buildingRow.getBuildingCards().contains(card1));
        assertTrue(buildingRow.getBuildingCards().contains(card2));
        assertTrue(targetRow.getBuildingCards().contains(card1));
        assertTrue(targetRow.getBuildingCards().contains(card2));
        assertTrue(targetRow.getBuildingCards().contains(card3));
    }

    @Test
    void isEmpty() {
        assertTrue(buildingRow.isEmpty());
        
        buildingRow.add(newBuilding(3));
        assertFalse(buildingRow.isEmpty());
        
        buildingRow.clear();
        assertTrue(buildingRow.isEmpty());
    }

    @Test
    void count() {
        assertEquals(0, buildingRow.count());
        
        buildingRow.add(newBuilding(3));
        assertEquals(1, buildingRow.count());
        
        buildingRow.add(newBuilding(5));
        buildingRow.add(newBuilding(2));
        assertEquals(3, buildingRow.count());
        
        buildingRow.remove(buildingRow.getBuildingCards().get(0));
        assertEquals(2, buildingRow.count());
        
        buildingRow.clear();
        assertEquals(0, buildingRow.count());
    }

    private BuildingCard newBuilding(int foodCost) {
        IBuildingEffect effect = new IBuildingEffect() {
        };
        return new BuildingCard(nextCardId++, "Building" + nextCardId, Era.ERA_1, 2, foodCost, 2, effect);
    }
}