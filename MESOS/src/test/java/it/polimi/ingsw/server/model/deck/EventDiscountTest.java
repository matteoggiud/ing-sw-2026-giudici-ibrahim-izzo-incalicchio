package it.polimi.ingsw.server.model.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventDiscountTest {

    private EventDiscount eventDiscount;

    @BeforeEach
    void setUp() {
        eventDiscount = new EventDiscount(2, 3);
    }

    /**
     * Tests adding positive and negative food values to the discount, verifying the
     * food property updates cumulatively and correctly.
     */
    @Test
    void addFood() {
        eventDiscount.addFood(4);
        assertEquals(7, eventDiscount.getFood());

        eventDiscount.addFood(-2);
        assertEquals(5, eventDiscount.getFood());
    }

    /**
     * Tests adding positive and negative prestige point values to the discount, verifying the
     * prestige points property updates cumulatively and correctly.
     */
    @Test
    void addPP() {
        eventDiscount.addPP(5);
        assertEquals(7, eventDiscount.getPP());

        eventDiscount.addPP(-3);
        assertEquals(4, eventDiscount.getPP());
    }

    /**
     * Tests retrieving the currently stored prestige points, including after subsequent additions
     * to the discount tracker.
     */
    @Test
    void getPP() {
        assertEquals(2, eventDiscount.getPP());

        eventDiscount.addPP(1);
        assertEquals(3, eventDiscount.getPP());
    }

    /**
     * Tests retrieving the currently stored food value, including after subsequent additions
     * to the discount tracker.
     */
    @Test
    void getFood() {
        assertEquals(3, eventDiscount.getFood());

        eventDiscount.addFood(2);
        assertEquals(5, eventDiscount.getFood());
    }
}