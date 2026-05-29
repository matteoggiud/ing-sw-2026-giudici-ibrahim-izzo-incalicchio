package it.polimi.ingsw.database;

import it.polimi.ingsw.server.database.DataDepository;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.EventCard;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DBTest {

    /**
     * Test that verifies the database connection is successfully established and prints all cards
     * retrieved from the database to standard output. It ensures that character, event, and building cards
     * can be correctly loaded, validating their non-null presence and facilitating manual inspection
     * of the loaded data.
     *
     * @throws SQLException if a database access error occurs
     */
    @Test
    void shouldConnectAndPrintAllCardsFromDatabase() throws SQLException {
        DataDepository dataDepository = new DataDepository();

        boolean connected = dataDepository.testConnection();
        assertTrue(connected, "Database connection failed");

        List<CharacterCard> characterCards = dataDepository.getCharacterCards();
        List<EventCard> eventCards = dataDepository.getEventCards();
        List<BuildingCard> buildingCards = dataDepository.getBuildingCards();

        assertNotNull(characterCards, "Character cards list must not be null");
        assertNotNull(eventCards, "Event cards list must not be null");
        assertNotNull(buildingCards, "Building cards list must not be null");

        System.out.println("\n========== DB CONNECTION TEST ==========");
        System.out.println("Connected: " + connected);
        System.out.println("========================================\n");

        printCharacterCards(characterCards);
        printEventCards(eventCards);
        printBuildingCards(buildingCards);
    }

    private void printCharacterCards(List<CharacterCard> characterCards) {
        System.out.println("----- CHARACTER CARDS (" + characterCards.size() + ") -----");
        for (CharacterCard card : characterCards) {
            System.out.println("name=" + card.getName()
                    + ", era=" + card.getEra()
                    + ", minPlayers=" + card.getMinNumPlayers()
                    + ", type=" + card.getClass().getSimpleName());
        }
        System.out.println();
    }

    private void printEventCards(List<EventCard> eventCards) {
        System.out.println("----- EVENT CARDS (" + eventCards.size() + ") -----");
        for (EventCard card : eventCards) {
            System.out.println("name=" + card.getName()
                    + ", era=" + card.getEra()
                    + ", minPlayers=" + card.getMinNumPlayers()
                    + ", finalEvent=" + card.isFinalEvent()
                    + ", effect=" + card.getEventEffect().getClass().getSimpleName()
                    + ", gainedFood=" + card.getGainedFood()
                    + ", malusFood=" + card.getMalusFood()
                    + ", gainedPP=" + card.getGainedPP()
                    + ", malusPP=" + card.getMalusPP()
                    + ", threshold=" + card.getThreshold());
        }
        System.out.println();
    }

    private void printBuildingCards(List<BuildingCard> buildingCards) {
        System.out.println("----- BUILDING CARDS (" + buildingCards.size() + ") -----");
        for (BuildingCard card : buildingCards) {
            System.out.println("name=" + card.getName()
                    + ", era=" + card.getEra()
                    + ", minPlayers=" + card.getMinNumPlayers()
                    + ", foodCost=" + card.getFoodCost()
                    + ", endGamePP=" + card.getEndGamePP()
                    + ", effect=" + card.getEffect().getClass().getSimpleName());
        }
        System.out.println();
    }
}

