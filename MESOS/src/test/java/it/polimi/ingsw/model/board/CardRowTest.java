package it.polimi.ingsw.model.board;

import it.polimi.ingsw.server.model.board.BuildingRow;
import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.characters.HunterCharacterCard;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.BuildingDeck;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.board.TribeRow;
import it.polimi.ingsw.server.model.era.Era1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardRowTest {

    private CardRow cardRow;
    private int nextCardId;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        cardRow = new CardRow();
        nextCardId = 1;
        era1 = new Era1();
    }

    /**
     * Tests retrieving the inner {@link TribeRow}.
     * Verifies that the retrieved instance is not null and is exactly the same instance 
     * stored internally by the card row.
     */
    @Test
    void getTribeRow() {
        TribeRow tribeRow = cardRow.getTribeRow();

        assertNotNull(tribeRow);
        assertSame(tribeRow, cardRow.getTribeRow());
    }

    /**
     * Tests retrieving the inner {@link BuildingRow}.
     * Verifies that the retrieved instance is not null and is exactly the same instance 
     * stored internally by the card row.
     */
    @Test
    void getBuildingRow() {
        BuildingRow buildingRow = cardRow.getBuildingRow();

        assertNotNull(buildingRow);
        assertSame(buildingRow, cardRow.getBuildingRow());
    }

    /**
     * Tests adding a card to the row.
     * Verifies that null cards are rejected with an exception, and valid cards
     * are properly delegated and added to the underlying tribe row.
     */
    @Test
    void addCard() {
        HunterCharacterCard hunter = newHunter();

        assertThrows(IllegalArgumentException.class, () -> cardRow.addCard(null));

        cardRow.addCard(hunter);

        assertEquals(1, cardRow.getTribeRow().count());
        assertTrue(cardRow.getTribeRow().getCharacterCards().contains(hunter));
    }

    /**
     * Tests clearing only the building row section of the card row.
     * Populates both tribe and building sections, clears the building section,
     * and verifies that buildings are removed but tribe cards remain.
     */
    @Test
    void clearBuildings() {
        cardRow.getBuildingRow().add(newBuilding(3));
        cardRow.addCard(newHunter());

        cardRow.clearBuildings();

        assertTrue(cardRow.getBuildingRow().isEmpty());
        assertEquals(1, cardRow.getTribeRow().count());
    }

    /**
     * Tests transferring buildings from a source {@link CardRow} to this card row.
     * Ensures that the source row's building row becomes empty and this row receives the buildings.
     */
    @Test
    void transferBuildingRowFrom() {
        CardRow source = new CardRow();
        source.getBuildingRow().add(newBuilding(3));
        source.getBuildingRow().add(newBuilding(4));

        cardRow.transferBuildingRowFrom(source);

        assertEquals(2, cardRow.getBuildingRow().count());
        assertTrue(source.getBuildingRow().isEmpty());
    }

    /**
     * Tests adding buildings directly from a {@link BuildingDeck}.
     * Populates a deck and transfers its cards, verifying that this card row's 
     * building row increases appropriately.
     */
    @Test
    void addBuildingRowFrom() {
        BuildingDeck deck = new BuildingDeck();
        deck.addBuildingCard(newBuilding(3));
        deck.addBuildingCard(newBuilding(4));

        cardRow.addBuildingRowFrom(deck);

        assertEquals(2, cardRow.getBuildingRow().count());
    }

    /**
     * Tests transferring tribe cards from a source {@link CardRow} to this card row.
     * Ensures that the source row's tribe row becomes empty and this row receives the cards.
     */
    @Test
    void transferTribeRowFrom() {
        CardRow source = new CardRow();
        source.addCard(newHunter());
        source.addCard(newHunter());

        cardRow.transferTribeRowFrom(source);

        assertEquals(2, cardRow.getTribeRow().count());
        assertTrue(source.getTribeRow().isEmpty());
    }

    /**
     * Tests fully clearing both sections of the card row.
     * Populates both tribe and building sections, calls clear,
     * and verifies that no cards remain in either section.
     */
    @Test
    void clearRow() {
        cardRow.addCard(newHunter());
        cardRow.getBuildingRow().add(newBuilding(3));

        cardRow.clearRow();

        assertTrue(cardRow.getTribeRow().isEmpty());
        assertTrue(cardRow.getBuildingRow().isEmpty());
        assertEquals(0, cardRow.countRow());
    }

    /**
     * Tests the aggregate count of the card row.
     * Verifies that the row correctly sums the sizes of both the tribe row and building row.
     */
    @Test
    void countRow() {
        cardRow.addCard(newHunter());
        cardRow.addCard(newHunter());
        cardRow.getBuildingRow().add(newBuilding(3));

        assertEquals(3, cardRow.countRow());
    }

    /**
     * Tests checking if there's any selectable card available for a specific player.
     * Verifies that if no cards exist, or if the available building is too expensive
     * compared to the player's food, it returns false. Adding enough food or adding
     * a free tribe card makes it return true.
     */
    @Test
    void hasSelectableCardFor() {
        Player player = new Player("p1");
        BuildingCard expensiveBuilding = newBuilding(3);

        assertFalse(cardRow.hasSelectableCardFor(player));

        cardRow.getBuildingRow().add(expensiveBuilding);
        assertFalse(cardRow.hasSelectableCardFor(player));

        player.addFood(3);
        assertTrue(cardRow.hasSelectableCardFor(player));

        cardRow.clearBuildings();
        cardRow.addCard(newHunter());
        assertTrue(cardRow.hasSelectableCardFor(player));
    }

    private HunterCharacterCard newHunter() {
        return new HunterCharacterCard(nextCardId++, "Hunter" + nextCardId, era1, 2, false,"");
    }

    private BuildingCard newBuilding(int foodCost) {
        IBuildingEffect effect = new IBuildingEffect() {
        };
        return new BuildingCard(nextCardId++, "Building" + nextCardId, era1, 2, foodCost, 2, effect,"");
    }
}