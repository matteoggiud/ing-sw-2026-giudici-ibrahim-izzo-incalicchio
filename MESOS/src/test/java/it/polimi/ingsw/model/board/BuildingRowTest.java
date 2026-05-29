package it.polimi.ingsw.model.board;

import it.polimi.ingsw.server.model.board.BuildingRow;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.deck.BuildingDeck;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.era.Era1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildingRowTest {

    private BuildingRow buildingRow;
    private int nextCardId;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        buildingRow = new BuildingRow();
        nextCardId = 1;
        era1 = new Era1();
    }

    /**
     * Tests adding individual {@link BuildingCard}s to the building row.
     * Verifies that the row updates its count and correctly stores the added cards.
     */
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

    /**
     * Tests adding multiple building cards directly from a {@link BuildingDeck}.
     * Verifies that all cards from the deck are successfully transferred to the building row.
     */
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

    /**
     * Tests removing a specific {@link BuildingCard} from the building row.
     * Verifies that the count decreases and only the specified card is removed while others remain.
     */
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

    /**
     * Tests clearing the entire building row.
     * Verifies that calling clear completely empties the row and resets the count to zero.
     */
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

    /**
     * Tests retrieving the list of building cards currently in the row.
     * Ensures that the retrieved list contains the correct cards and that it is
     * returned as a defensive copy to prevent external modification of the internal state.
     */
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

    /**
     * Tests transferring building cards from another {@link BuildingRow}.
     * Verifies that the target row receives all cards from the source row, 
     * and that the source row becomes empty afterwards.
     */
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

    /**
     * Tests copying building cards to another {@link BuildingRow}.
     * Verifies that the cards from this row are added to the target row without being
     * removed from the source row.
     */
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

    /**
     * Tests checking if the building row is empty.
     * Verifies that it returns true initially, false after adding cards, and true again after clearing.
     */
    @Test
    void isEmpty() {
        assertTrue(buildingRow.isEmpty());
        
        buildingRow.add(newBuilding(3));
        assertFalse(buildingRow.isEmpty());
        
        buildingRow.clear();
        assertTrue(buildingRow.isEmpty());
    }

    /**
     * Tests counting the total number of building cards in the row.
     * Verifies that the count appropriately reflects additions, removals, and clearing of the row.
     */
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
        return new BuildingCard(nextCardId++, "Building" + nextCardId, era1, 2, foodCost, 2, effect,"");
    }
}