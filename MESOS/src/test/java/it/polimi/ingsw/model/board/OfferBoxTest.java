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

    /**
     * Tests the validation logic in the {@link OfferBox} constructor.
     * Ensures that exceptions are thrown for combinations that violate the rules 
     * (e.g., negative values, combined food/card actions, or invalid total actions),
     * and that valid configurations are created successfully.
     */
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

    /**
     * Tests retrieving the food reward of an offer box.
     * Verifies that the correct initialized value is returned across various valid configurations.
     */
    @Test
    void getFoodReward() {
        assertEquals(0, offerBox.getFoodReward());

        OfferBox zeroRewardBox = new OfferBox(0, 2, 1);
        assertEquals(0, zeroRewardBox.getFoodReward());

        OfferBox threeRewardBox = new OfferBox(3, 0, 0);
        assertEquals(3, threeRewardBox.getFoodReward());
    }

    /**
     * Tests retrieving the upper row pick amount from the offer box.
     * Verifies that the initialized upper row picks are properly stored and returned.
     */
    @Test
    void getUpperRowPicks() {
        assertEquals(2, offerBox.getUpperRowPicks());

        OfferBox zeroPicks = new OfferBox(0, 0, 2);
        assertEquals(0, zeroPicks.getUpperRowPicks());

        OfferBox threePicks = new OfferBox(0, 3, 1);
        assertEquals(3, threePicks.getUpperRowPicks());
    }

    /**
     * Tests retrieving the lower row pick amount from the offer box.
     * Verifies that the initialized lower row picks are properly stored and returned.
     */
    @Test
    void getLowerRowPicks() {
        assertEquals(1, offerBox.getLowerRowPicks());

        OfferBox zeroPicks = new OfferBox(0, 2, 0);
        assertEquals(0, zeroPicks.getLowerRowPicks());

        OfferBox threePicks = new OfferBox(0, 1, 3);
        assertEquals(3, threePicks.getLowerRowPicks());
    }

    /**
     * Tests the evaluation of whether an offer box provides any card picks.
     * Verifies it returns true if there is at least one pick from either the upper or lower row,
     * and false otherwise.
     */
    @Test
    void hasCardPicks() {
        assertTrue(new OfferBox(0, 1, 0).hasCardPicks());
        assertTrue(new OfferBox(0, 0, 1).hasCardPicks());
        assertFalse(new OfferBox(3, 0, 0).hasCardPicks());
    }

    /**
     * Tests the evaluation of whether an offer box provides only a food reward.
     * Verifies it returns true when there are food rewards and no card picks, and false otherwise.
     */
    @Test
    void isFoodOnlyAction() {
        assertTrue(new OfferBox(3, 0, 0).isFoodOnlyAction());
        assertFalse(new OfferBox(0, 1, 0).isFoodOnlyAction());
        assertFalse(new OfferBox(0, 0, 1).isFoodOnlyAction());
    }

    /**
     * Tests retrieving the player placed on the offer box.
     * Checks that it is initially null, properly returns the placed player, and reverts
     * to null after the player is removed.
     */
    @Test
    void getPlacedPlayer() {
        assertNull(offerBox.getPlacedPlayer());

        offerBox.placePlayer(player1);
        assertNotNull(offerBox.getPlacedPlayer());
        assertSame(player1, offerBox.getPlacedPlayer());

        offerBox.removePlayer();
        assertNull(offerBox.getPlacedPlayer());
    }

    /**
     * Tests the free/occupied status of the offer box.
     * Verifies that the box returns true when unoccupied and false when a player is currently placed on it.
     */
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

    /**
     * Tests placing a player on the offer box.
     * Checks that a free box can accept a player, and an occupied box rejects further placements
     * by throwing an {@link IllegalStateException}.
     */
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

    /**
     * Tests placing a null player on the offer box.
     * Ensures that an {@link IllegalArgumentException} is thrown and the box remains free.
     */
    @Test
    void placePlayerNull() {
        assertThrows(IllegalArgumentException.class, () ->
                offerBox.placePlayer(null)
        );

        assertTrue(offerBox.isFree());
    }

    /**
     * Tests removing a player from the offer box.
     * Verifies that calling it on an empty box safely returns null, while calling it on an
     * occupied box returns the correct player and marks the box as free again.
     */
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

    /**
     * Tests multiple consecutive sequences of placing and removing players.
     * Ensures the internal state of the offer box remains consistent across multiple state changes.
     */
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
