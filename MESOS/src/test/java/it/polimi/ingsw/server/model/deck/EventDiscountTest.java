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

    @Test
    void addFood() {
        eventDiscount.addFood(4);
        assertEquals(7, eventDiscount.getFood());

        eventDiscount.addFood(-2);
        assertEquals(5, eventDiscount.getFood());
    }

    @Test
    void addPP() {
        eventDiscount.addPP(5);
        assertEquals(7, eventDiscount.getPP());

        eventDiscount.addPP(-3);
        assertEquals(4, eventDiscount.getPP());
    }

    @Test
    void getPP() {
        assertEquals(2, eventDiscount.getPP());

        eventDiscount.addPP(1);
        assertEquals(3, eventDiscount.getPP());
    }

    @Test
    void getFood() {
        assertEquals(3, eventDiscount.getFood());

        eventDiscount.addFood(2);
        assertEquals(5, eventDiscount.getFood());
    }
}