package it.polimi.ingsw.model.board;

import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.core.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfferBoxTest {

    private OfferBox offerBox;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        offerBox = new OfferBox(0, 2, 1);
        player1 = new Player("Player1");
        player2 = new Player("Player2");
    }

    @Test
    void constructorValidation() {
        assertThrows(IllegalArgumentException.class, () ->
                new OfferBox(1, 2, 1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new OfferBox(5, 2, 1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new OfferBox(0, -1, 1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new OfferBox(3, 2, -1)
        );

        OfferBox validCardBox = new OfferBox(0, 2, 1);
        assertNotNull(validCardBox);

        OfferBox validFoodBox = new OfferBox(3, 0, 0);
        assertNotNull(validFoodBox);
    }

    @Test
    void getFoodReward() {
        assertEquals(0, offerBox.getFoodReward());

        OfferBox zeroRewardBox = new OfferBox(0, 2, 1);
        assertEquals(0, zeroRewardBox.getFoodReward());

        OfferBox threeRewardBox = new OfferBox(3, 0, 0);
        assertEquals(3, threeRewardBox.getFoodReward());
    }

    @Test
    void getUpperRowPicks() {
        assertEquals(2, offerBox.getUpperRowPicks());

        OfferBox zeroPicks = new OfferBox(0, 0, 2);
        assertEquals(0, zeroPicks.getUpperRowPicks());

        OfferBox threePicks = new OfferBox(0, 3, 1);
        assertEquals(3, threePicks.getUpperRowPicks());
    }

    @Test
    void getLowerRowPicks() {
        assertEquals(1, offerBox.getLowerRowPicks());

        OfferBox zeroPicks = new OfferBox(0, 2, 0);
        assertEquals(0, zeroPicks.getLowerRowPicks());

        OfferBox threePicks = new OfferBox(0, 1, 3);
        assertEquals(3, threePicks.getLowerRowPicks());
    }

    @Test
    void hasCardPicks() {
        assertTrue(new OfferBox(0, 1, 0).hasCardPicks());
        assertTrue(new OfferBox(0, 0, 1).hasCardPicks());
        assertFalse(new OfferBox(3, 0, 0).hasCardPicks());
    }

    @Test
    void isFoodOnlyAction() {
        assertTrue(new OfferBox(3, 0, 0).isFoodOnlyAction());
        assertFalse(new OfferBox(0, 1, 0).isFoodOnlyAction());
        assertFalse(new OfferBox(0, 0, 1).isFoodOnlyAction());
    }

    @Test
    void getPlacedPlayer() {
        assertNull(offerBox.getPlacedPlayer());

        offerBox.placePlayer(player1);
        assertNotNull(offerBox.getPlacedPlayer());
        assertSame(player1, offerBox.getPlacedPlayer());

        offerBox.removePlayer();
        assertNull(offerBox.getPlacedPlayer());
    }

    @Test
    void isFree() {
        assertTrue(offerBox.isFree());

        offerBox.placePlayer(player1);
        assertFalse(offerBox.isFree());

        offerBox.removePlayer();
        assertTrue(offerBox.isFree());

        offerBox.placePlayer(player2);
        assertFalse(offerBox.isFree());
    }

    @Test
    void placePlayer() {
        assertTrue(offerBox.isFree());

        offerBox.placePlayer(player1);
        assertFalse(offerBox.isFree());
        assertSame(player1, offerBox.getPlacedPlayer());

        assertThrows(IllegalStateException.class, () ->
                offerBox.placePlayer(player2)
        );

        assertSame(player1, offerBox.getPlacedPlayer());
    }

    @Test
    void placePlayerNull() {
        assertThrows(IllegalArgumentException.class, () ->
                offerBox.placePlayer(null)
        );

        assertTrue(offerBox.isFree());
    }

    @Test
    void removePlayer() {
        assertNull(offerBox.removePlayer());
        assertTrue(offerBox.isFree());

        offerBox.placePlayer(player1);
        assertFalse(offerBox.isFree());

        Player removed = offerBox.removePlayer();
        assertNotNull(removed);
        assertSame(player1, removed);
        assertTrue(offerBox.isFree());
        assertNull(offerBox.getPlacedPlayer());

        assertNull(offerBox.removePlayer());
        assertTrue(offerBox.isFree());
    }

    @Test
    void multipleOperations() {
        assertTrue(offerBox.isFree());

        offerBox.placePlayer(player1);
        assertSame(player1, offerBox.getPlacedPlayer());
        assertFalse(offerBox.isFree());

        Player removed1 = offerBox.removePlayer();
        assertSame(player1, removed1);
        assertTrue(offerBox.isFree());

        offerBox.placePlayer(player2);
        assertSame(player2, offerBox.getPlacedPlayer());
        assertFalse(offerBox.isFree());

        Player removed2 = offerBox.removePlayer();
        assertSame(player2, removed2);
        assertTrue(offerBox.isFree());
    }
}
