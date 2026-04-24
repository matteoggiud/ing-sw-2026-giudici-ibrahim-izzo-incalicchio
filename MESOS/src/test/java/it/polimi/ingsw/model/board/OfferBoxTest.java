package it.polimi.ingsw.model.board;

import it.polimi.ingsw.server.model.board.OFFER_BOX_ID;
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
        offerBox = new OfferBox(OFFER_BOX_ID.A, 3, 2, 1);
        player1 = new Player("Player1");
        player2 = new Player("Player2");
    }

    @Test
    void constructorValidation() {
        // Test null boxId
        assertThrows(IllegalArgumentException.class, () ->
            new OfferBox(null, 0, 2, 1)
        );

        // Test invalid foodReward (not 0 or 3)
        assertThrows(IllegalArgumentException.class, () ->
            new OfferBox(OFFER_BOX_ID.B, 1, 2, 1)
        );
        
        assertThrows(IllegalArgumentException.class, () ->
            new OfferBox(OFFER_BOX_ID.B, 5, 2, 1)
        );

        // Test negative upperRowPicks
        assertThrows(IllegalArgumentException.class, () ->
            new OfferBox(OFFER_BOX_ID.C, 0, -1, 1)
        );

        // Test negative lowerRowPicks
        assertThrows(IllegalArgumentException.class, () ->
            new OfferBox(OFFER_BOX_ID.D, 3, 2, -1)
        );

        // Test valid constructor with foodReward 0
        OfferBox validBox = new OfferBox(OFFER_BOX_ID.E, 0, 2, 1);
        assertNotNull(validBox);
        
        // Test valid constructor with foodReward 3
        OfferBox validBox2 = new OfferBox(OFFER_BOX_ID.F, 3, 2, 1);
        assertNotNull(validBox2);
    }

    @Test
    void getBoxId() {
        assertEquals(OFFER_BOX_ID.A, offerBox.getBoxId());
        
        OfferBox boxB = new OfferBox(OFFER_BOX_ID.B, 0, 1, 2);
        assertEquals(OFFER_BOX_ID.B, boxB.getBoxId());
        
        OfferBox boxG = new OfferBox(OFFER_BOX_ID.G, 3, 2, 2);
        assertEquals(OFFER_BOX_ID.G, boxG.getBoxId());
    }

    @Test
    void getFoodReward() {
        assertEquals(3, offerBox.getFoodReward());
        
        OfferBox zeroRewardBox = new OfferBox(OFFER_BOX_ID.B, 0, 2, 1);
        assertEquals(0, zeroRewardBox.getFoodReward());
        
        OfferBox threeRewardBox = new OfferBox(OFFER_BOX_ID.C, 3, 2, 1);
        assertEquals(3, threeRewardBox.getFoodReward());
    }

    @Test
    void getUpperRowPicks() {
        assertEquals(2, offerBox.getUpperRowPicks());
        
        OfferBox zeroPicks = new OfferBox(OFFER_BOX_ID.B, 0, 0, 2);
        assertEquals(0, zeroPicks.getUpperRowPicks());
        
        OfferBox threePicks = new OfferBox(OFFER_BOX_ID.C, 3, 3, 1);
        assertEquals(3, threePicks.getUpperRowPicks());
    }

    @Test
    void getLowerRowPicks() {
        assertEquals(1, offerBox.getLowerRowPicks());
        
        OfferBox zeroPicks = new OfferBox(OFFER_BOX_ID.B, 0, 2, 0);
        assertEquals(0, zeroPicks.getLowerRowPicks());
        
        OfferBox threePicks = new OfferBox(OFFER_BOX_ID.C, 3, 1, 3);
        assertEquals(3, threePicks.getLowerRowPicks());
    }

    @Test
    void getPlacedPlayer() {
        // Initially should be null
        assertNull(offerBox.getPlacedPlayer());
        
        // Place a player
        offerBox.placePlayer(player1);
        assertNotNull(offerBox.getPlacedPlayer());
        assertSame(player1, offerBox.getPlacedPlayer());
        
        // Remove the player
        offerBox.removePlayer();
        assertNull(offerBox.getPlacedPlayer());
    }

    @Test
    void isFree() {
        // Initially should be free
        assertTrue(offerBox.isFree());
        
        // Place a player
        offerBox.placePlayer(player1);
        assertFalse(offerBox.isFree());
        
        // Remove the player
        offerBox.removePlayer();
        assertTrue(offerBox.isFree());
        
        // Place another player
        offerBox.placePlayer(player2);
        assertFalse(offerBox.isFree());
    }

    @Test
    void placePlayer() {
        assertTrue(offerBox.isFree());
        
        // Place a valid player
        offerBox.placePlayer(player1);
        assertFalse(offerBox.isFree());
        assertSame(player1, offerBox.getPlacedPlayer());
        
        // Try to place another player on occupied box - should throw
        assertThrows(IllegalStateException.class, () ->
            offerBox.placePlayer(player2)
        );
        
        // Verify player1 is still placed
        assertSame(player1, offerBox.getPlacedPlayer());
    }

    @Test
    void placePlayerNull() {
        // Try to place null player
        assertThrows(IllegalArgumentException.class, () ->
            offerBox.placePlayer(null)
        );
        
        // Verify box is still free
        assertTrue(offerBox.isFree());
    }

    @Test
    void removePlayer() {
        // Remove from empty box - should return null
        assertNull(offerBox.removePlayer());
        assertTrue(offerBox.isFree());
        
        // Place a player
        offerBox.placePlayer(player1);
        assertFalse(offerBox.isFree());
        
        // Remove the player
        Player removed = offerBox.removePlayer();
        assertNotNull(removed);
        assertSame(player1, removed);
        assertTrue(offerBox.isFree());
        assertNull(offerBox.getPlacedPlayer());
        
        // Remove again - should return null
        assertNull(offerBox.removePlayer());
        assertTrue(offerBox.isFree());
    }

    @Test
    void multipleOperations() {
        // Scenario: place player1, remove, place player2, remove
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