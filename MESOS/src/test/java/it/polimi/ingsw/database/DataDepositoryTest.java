package it.polimi.ingsw.database;

import it.polimi.ingsw.server.database.DataDepository;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.card.TribeCard;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class DataDepositoryTest {

    private final DataDepository dataDepository = new DataDepository();

    /**
     * Test that the database connection can be established successfully.
     *
     * @throws SQLException if a database access error occurs
     */
    @Test
    void testConnection() throws SQLException {
        assertTrue(dataDepository.testConnection(), "Database not reachable: connection test failed");
    }

    /**
     * Test that getAllTribeCards returns a non-null, non-empty list of TribeCard objects with valid card IDs.
     *
     * @throws SQLException if a database access error occurs
     */
    @Test
    void getAllTribeCards_shouldLoadCardsWithValidIds() throws SQLException {
        assumeTrue(dataDepository.testConnection(), "Database not reachable: skipping test");

        List<TribeCard> cards = dataDepository.getAllTribeCards();

        assertNotNull(cards, "The card list must not be null");
        assertFalse(cards.isEmpty(), "The card list must not be empty");
        assertTrue(cards.stream().allMatch(c -> c.getCardId() > 0), "Each card must have cardId valorised by the DB");
    }

    @Test
    void getCharacterCards_shouldLoadNonEmptyCardsWithValidIds() throws SQLException {
        assumeTrue(dataDepository.testConnection(), "Database not reachable: skipping test");

        List<CharacterCard> cards = dataDepository.getCharacterCards();

        assertNotNull(cards, "Character cards list must not be null");
        assertFalse(cards.isEmpty(), "Character cards list must not be empty");
        assertTrue(cards.stream().allMatch(c -> c.getCardId() > 0), "Every character card must have a valid DB id");
        assertTrue(cards.stream().allMatch(c -> c.getName() != null && !c.getName().isBlank()), "Character names must be valorizzati");
    }

    @Test
    void getEventCards_shouldLoadValidEventCards() throws SQLException {
        assumeTrue(dataDepository.testConnection(), "Database not reachable: skipping test");

        List<EventCard> cards = dataDepository.getEventCards();

        assertNotNull(cards, "Event cards list must not be null");
        assertFalse(cards.isEmpty(), "Event cards list must not be empty");
        assertTrue(cards.stream().allMatch(c -> c.getCardId() > 0), "Every event card must have a valid DB id");
        assertTrue(cards.stream().allMatch(EventCard::goesInUpperRow), "Every event card should be placeable in upper row");
        assertTrue(cards.stream().allMatch(c -> c.getEventEffect() != null), "Every event card must have a non-null effect");
    }

    @Test
    void getBuildingCards_shouldLoadValidBuildingCards() throws SQLException {
        assumeTrue(dataDepository.testConnection(), "Database not reachable: skipping test");

        List<BuildingCard> cards = dataDepository.getBuildingCards();

        assertNotNull(cards, "Building cards list must not be null");
        assertFalse(cards.isEmpty(), "Building cards list must not be empty");
        assertTrue(cards.stream().allMatch(c -> c.getCardId() > 0), "Every building card must have a valid DB id");
        assertTrue(cards.stream().allMatch(c -> c.getEffect() != null), "Every building card must have a non-null effect");
        assertTrue(cards.stream().allMatch(c -> c.getFoodCost() >= 0), "Food cost must be non-negative");
    }

    @Test
    void getAllTribeCards_shouldMatchCharacterPlusEventCounts() throws SQLException {
        assumeTrue(dataDepository.testConnection(), "Database not reachable: skipping test");

        int expected = dataDepository.getCharacterCards().size() + dataDepository.getEventCards().size();
        int actual = dataDepository.getAllTribeCards().size();

        assertEquals(expected, actual, "All tribe cards should be the sum of character and event cards");
    }
}
