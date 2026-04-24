package it.polimi.ingsw.server.model.core;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.Era;
import it.polimi.ingsw.server.model.card.characters.HunterCharacterCard;
import it.polimi.ingsw.server.model.deck.BuildingDeck;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private int nextCardId;

    @BeforeEach
    void setUp() {
        board = new Board();
        nextCardId = 1;
    }

    @Test
    void getOfferTrack() {
        assertNotNull(board.getOfferTrack());
        assertSame(board.getOfferTrack(), board.getOfferTrack());
    }

    @Test
    void getUpperRow() {
        assertNotNull(board.getUpperRow());
        assertSame(board.getUpperRow(), board.getUpperRow());
        assertEquals(0, board.getUpperRow().countRow());
    }

    @Test
    void getLowerRow() {
        assertNotNull(board.getLowerRow());
        assertSame(board.getLowerRow(), board.getLowerRow());
        assertEquals(0, board.getLowerRow().countRow());
    }

    @Test
    void getTurnOrderBox() {
        assertNotNull(board.getTurnOrderBox());
        assertSame(board.getTurnOrderBox(), board.getTurnOrderBox());
        assertTrue(board.getTurnOrderBox().isCompletelyEmpty());
    }

    @Test
    void addTribeUpperRow() {
        HunterCharacterCard hunter = newHunter();

        assertThrows(IllegalArgumentException.class, () -> board.addTribeUpperRow(null));

        board.addTribeUpperRow(hunter);

        assertEquals(1, board.getUpperRow().getTribeRow().count());
        assertTrue(board.getUpperRow().getTribeRow().getCharacterCards().contains(hunter));
    }

    @Test
    void clearLowerBuildingRow() {
        board.getLowerRow().getBuildingRow().add(newBuilding(3));
        board.getLowerRow().addCard(newHunter());

        board.clearLowerBuildingRow();

        assertTrue(board.getLowerRow().getBuildingRow().isEmpty());
        assertEquals(1, board.getLowerRow().getTribeRow().count());
    }

    @Test
    void transferUpperBuildingToLower() {
        BuildingCard b1 = newBuilding(3);
        BuildingCard b2 = newBuilding(4);
        BuildingCard existingLower = newBuilding(2);

        board.getUpperRow().getBuildingRow().add(b1);
        board.getUpperRow().getBuildingRow().add(b2);
        board.getLowerRow().getBuildingRow().add(existingLower);

        board.transferUpperBuildingToLower();

        assertEquals(0, board.getUpperRow().getBuildingRow().count());
        assertEquals(3, board.getLowerRow().getBuildingRow().count());
        assertTrue(board.getLowerRow().getBuildingRow().getBuildingCards().contains(b1));
        assertTrue(board.getLowerRow().getBuildingRow().getBuildingCards().contains(b2));
        assertTrue(board.getLowerRow().getBuildingRow().getBuildingCards().contains(existingLower));
    }

    @Test
    void placeInUpperBuildingRow() {
        BuildingDeck deck = new BuildingDeck();
        BuildingCard b1 = newBuilding(3);
        BuildingCard b2 = newBuilding(5);
        deck.addBuildingCard(b1);
        deck.addBuildingCard(b2);

        board.placeInUpperBuildingRow(deck);

        assertEquals(2, board.getUpperRow().getBuildingRow().count());
        assertTrue(board.getUpperRow().getBuildingRow().getBuildingCards().contains(b1));
        assertTrue(board.getUpperRow().getBuildingRow().getBuildingCards().contains(b2));
    }

    @Test
    void transferUpperTribeToLower() {
        HunterCharacterCard h1 = newHunter();
        HunterCharacterCard h2 = newHunter();
        HunterCharacterCard existingLower = newHunter();

        board.getUpperRow().addCard(h1);
        board.getUpperRow().addCard(h2);
        board.getLowerRow().addCard(existingLower);

        board.transferUpperTribeToLower();

        assertEquals(0, board.getUpperRow().getTribeRow().count());
        assertEquals(3, board.getLowerRow().getTribeRow().count());
        assertTrue(board.getLowerRow().getTribeRow().getCharacterCards().contains(h1));
        assertTrue(board.getLowerRow().getTribeRow().getCharacterCards().contains(h2));
        assertTrue(board.getLowerRow().getTribeRow().getCharacterCards().contains(existingLower));
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