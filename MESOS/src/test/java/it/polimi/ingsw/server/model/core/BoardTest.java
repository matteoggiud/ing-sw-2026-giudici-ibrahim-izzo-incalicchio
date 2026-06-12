package it.polimi.ingsw.server.model.core;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.card.characters.HunterCharacterCard;
import it.polimi.ingsw.server.model.deck.BuildingDeck;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private int nextCardId;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        board = new Board();
        nextCardId = 1;
        era1 = new Era1();
    }

    /**
     * Tests getting the offer track from the board.
     * Validates that {@link Board#getOfferTrack()} correctly returns the instantiated offer track.
     */
    @Test
    void getOfferTrack() {
        assertNotNull(board.getOfferTrack());
        assertSame(board.getOfferTrack(), board.getOfferTrack());
    }

    /**
     * Tests getting the upper row from the board.
     * Validates that {@link Board#getUpperRow()} correctly returns the instantiated upper row, starting empty.
     */
    @Test
    void getUpperRow() {
        assertNotNull(board.getUpperRow());
        assertSame(board.getUpperRow(), board.getUpperRow());
        assertEquals(0, board.getUpperRow().countRow());
    }

    /**
     * Tests getting the lower row from the board.
     * Validates that {@link Board#getLowerRow()} correctly returns the instantiated lower row, starting empty.
     */
    @Test
    void getLowerRow() {
        assertNotNull(board.getLowerRow());
        assertSame(board.getLowerRow(), board.getLowerRow());
        assertEquals(0, board.getLowerRow().countRow());
    }

    /**
     * Tests getting the turn order box.
     * Validates that {@link Board#getTurnOrderBox()} returns an initialized and empty turn order box.
     */
    @Test
    void getTurnOrderBox() {
        assertNotNull(board.getTurnOrderBox());
        assertSame(board.getTurnOrderBox(), board.getTurnOrderBox());
        assertTrue(board.getTurnOrderBox().isCompletelyEmpty());
    }

    /**
     * Tests adding a tribe card to the upper row.
     * Validates that {@link Board#addTribeUpperRow(Card)} correctly places the tribe card in the upper row and rejects nulls.
     */
    @Test
    void addTribeUpperRow() {
        HunterCharacterCard hunter = newHunter();

        assertThrows(IllegalArgumentException.class, () -> board.addTribeUpperRow(null));

        board.addTribeUpperRow(hunter);

        assertEquals(1, board.getUpperRow().getTribeRow().count());
        assertTrue(board.getUpperRow().getTribeRow().getCharacterCards().contains(hunter));
    }

    /**
     * Tests clearing the building cards from the lower row.
     * Validates that {@link Board#clearLowerBuildingRow()} successfully empties the building portion while preserving tribe cards.
     */
    @Test
    void clearLowerBuildingRow() {
        board.getLowerRow().getBuildingRow().add(newBuilding(3));
        board.getLowerRow().addCard(newHunter());

        board.clearLowerBuildingRow();

        assertTrue(board.getLowerRow().getBuildingRow().isEmpty());
        assertEquals(1, board.getLowerRow().getTribeRow().count());
    }

    /**
     * Tests transferring building cards from the upper row down to the lower row.
     * Validates that {@link Board#transferUpperBuildingToLower()} clears upper buildings and aggregates them into the lower row.
     */
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

    /**
     * Tests drawing and placing new building cards into the upper row from a deck.
     * Validates that {@link Board#placeInUpperBuildingRow(BuildingDeck)} shifts cards from the deck successfully.
     */
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

    /**
     * Tests transferring tribe cards from the upper row down to the lower row.
     * Validates that {@link Board#transferUpperTribeToLower()} successfully consolidates cards downwards into the lower row.
     */
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
        return new HunterCharacterCard(nextCardId++, "Hunter" + nextCardId, era1, 2, false,"");
    }

    private BuildingCard newBuilding(int foodCost) {
        IBuildingEffect effect = new IBuildingEffect() {
        };
        return new BuildingCard(nextCardId++, "Building" + nextCardId, era1, 2, foodCost, 2, effect,"");
    }
}