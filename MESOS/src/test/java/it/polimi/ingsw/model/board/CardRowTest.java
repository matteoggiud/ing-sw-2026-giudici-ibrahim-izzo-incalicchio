package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.card.BuildingCard;
import it.polimi.ingsw.model.card.Era;
import it.polimi.ingsw.model.card.characters.HunterCharacterCard;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.deck.BuildingDeck;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardRowTest {

    private CardRow cardRow;
    private int nextCardId;

    @BeforeEach
    void setUp() {
        cardRow = new CardRow();
        nextCardId = 1;
    }

    @Test
    void getTribeRow() {
        TribeRow tribeRow = cardRow.getTribeRow();

        assertNotNull(tribeRow);
        assertSame(tribeRow, cardRow.getTribeRow());
    }

    @Test
    void getBuildingRow() {
        BuildingRow buildingRow = cardRow.getBuildingRow();

        assertNotNull(buildingRow);
        assertSame(buildingRow, cardRow.getBuildingRow());
    }

    @Test
    void addCard() {
        HunterCharacterCard hunter = newHunter();

        assertThrows(IllegalArgumentException.class, () -> cardRow.addCard(null));

        cardRow.addCard(hunter);

        assertEquals(1, cardRow.getTribeRow().count());
        assertTrue(cardRow.getTribeRow().getCharacterCards().contains(hunter));
    }

    @Test
    void clearBuildings() {
        cardRow.getBuildingRow().add(newBuilding(3));
        cardRow.addCard(newHunter());

        cardRow.clearBuildings();

        assertTrue(cardRow.getBuildingRow().isEmpty());
        assertEquals(1, cardRow.getTribeRow().count());
    }

    @Test
    void transferBuildingRowFrom() {
        CardRow source = new CardRow();
        source.getBuildingRow().add(newBuilding(3));
        source.getBuildingRow().add(newBuilding(4));

        cardRow.transferBuildingRowFrom(source);

        assertEquals(2, cardRow.getBuildingRow().count());
        assertTrue(source.getBuildingRow().isEmpty());
    }

    @Test
    void addBuildingRowFrom() {
        BuildingDeck deck = new BuildingDeck();
        deck.addBuildingCard(newBuilding(3));
        deck.addBuildingCard(newBuilding(4));

        cardRow.addBuildingRowFrom(deck);

        assertEquals(2, cardRow.getBuildingRow().count());
    }

    @Test
    void transferTribeRowFrom() {
        CardRow source = new CardRow();
        source.addCard(newHunter());
        source.addCard(newHunter());

        cardRow.transferTribeRowFrom(source);

        assertEquals(2, cardRow.getTribeRow().count());
        assertTrue(source.getTribeRow().isEmpty());
    }

    @Test
    void clearRow() {
        cardRow.addCard(newHunter());
        cardRow.getBuildingRow().add(newBuilding(3));

        cardRow.clearRow();

        assertTrue(cardRow.getTribeRow().isEmpty());
        assertTrue(cardRow.getBuildingRow().isEmpty());
        assertEquals(0, cardRow.countRow());
    }

    @Test
    void countRow() {
        cardRow.addCard(newHunter());
        cardRow.addCard(newHunter());
        cardRow.getBuildingRow().add(newBuilding(3));

        assertEquals(3, cardRow.countRow());
    }

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
        return new HunterCharacterCard(nextCardId++, "Hunter" + nextCardId, Era.ERA_1, 2, false);
    }

    private BuildingCard newBuilding(int foodCost) {
        IBuildingEffect effect = new IBuildingEffect() {
        };
        return new BuildingCard(nextCardId++, "Building" + nextCardId, Era.ERA_1, 2, foodCost, 2, effect);
    }
}