package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.era.Era2;
import it.polimi.ingsw.server.model.era.Era3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildingDecksTest {

    private BuildingDecks buildingDecks;
    private int nextCardId;
    private Era1 era1;
    private Era2 era2;
    private Era3 era3;

    @BeforeEach
    void setUp() {
        buildingDecks = new BuildingDecks();
        nextCardId = 1;
        era1 = new Era1();
        era2 = new Era2();
        era3 = new Era3();
    }

    @Test
    void getBuildingDeck() {
        BuildingDeck era1Deck = buildingDecks.getBuildingDeck(era1);
        BuildingDeck era2Deck = buildingDecks.getBuildingDeck(era2);
        BuildingDeck era3Deck = buildingDecks.getBuildingDeck(era3);

        assertNotNull(era1Deck);
        assertNotNull(era2Deck);
        assertNotNull(era3Deck);
        assertTrue(era1Deck.getBuildingCards().isEmpty());
        assertTrue(era2Deck.getBuildingCards().isEmpty());
        assertTrue(era3Deck.getBuildingCards().isEmpty());
        assertSame(era1Deck, buildingDecks.getBuildingDeck(era1));
    }

    @Test
    void addBuildingCards() {
        BuildingCard era1a = newBuilding(era1, 3);
        BuildingCard era1b = newBuilding(era1, 4);
        BuildingCard era2Card = newBuilding(era2, 5);
        BuildingCard era3Card = newBuilding(era3, 6);

        List<BuildingCard> cards = List.of(era1a, era1b, era2Card, era3Card);
        buildingDecks.addBuildingCards(cards);

        assertEquals(2, buildingDecks.getBuildingDeck(era1).getBuildingCards().size());
        assertEquals(1, buildingDecks.getBuildingDeck(era2).getBuildingCards().size());
        assertEquals(1, buildingDecks.getBuildingDeck(era3).getBuildingCards().size());
        assertTrue(buildingDecks.getBuildingDeck(era1).getBuildingCards().contains(era1a));
        assertTrue(buildingDecks.getBuildingDeck(era1).getBuildingCards().contains(era1b));
        assertTrue(buildingDecks.getBuildingDeck(era2).getBuildingCards().contains(era2Card));
        assertTrue(buildingDecks.getBuildingDeck(era3).getBuildingCards().contains(era3Card));

        assertThrows(IllegalArgumentException.class, () -> buildingDecks.addBuildingCards(null));
    }

    @Test
    void setBuildingDeck() {
        BuildingCard oldEra1 = newBuilding(era1, 3);
        BuildingCard oldEra2 = newBuilding(era2, 4);
        buildingDecks.addBuildingCards(List.of(oldEra1, oldEra2));

        BuildingCard newEra1 = newBuilding(era1, 5);
        BuildingCard newEra3a = newBuilding(era3, 6);
        BuildingCard newEra3b = newBuilding(era3, 7);
        ArrayList<BuildingCard> replacement = new ArrayList<>(List.of(newEra1, newEra3a, newEra3b));

        buildingDecks.setBuildingDeck(replacement);

        assertEquals(1, buildingDecks.getBuildingDeck(era1).getBuildingCards().size());
        assertEquals(0, buildingDecks.getBuildingDeck(era2).getBuildingCards().size());
        assertEquals(2, buildingDecks.getBuildingDeck(era3).getBuildingCards().size());
        assertTrue(buildingDecks.getBuildingDeck(era1).getBuildingCards().contains(newEra1));
        assertTrue(buildingDecks.getBuildingDeck(era3).getBuildingCards().contains(newEra3a));
        assertTrue(buildingDecks.getBuildingDeck(era3).getBuildingCards().contains(newEra3b));
        assertFalse(buildingDecks.getBuildingDeck(era1).getBuildingCards().contains(oldEra1));
        assertFalse(buildingDecks.getBuildingDeck(era2).getBuildingCards().contains(oldEra2));

        assertThrows(IllegalArgumentException.class, () -> buildingDecks.setBuildingDeck(null));
        assertThrows(IllegalArgumentException.class, () -> buildingDecks.setBuildingDeck(List.of()));
    }

    private BuildingCard newBuilding(Era era, int foodCost) {
        it.polimi.ingsw.server.model.effect.building.IBuildingEffect effect = new it.polimi.ingsw.server.model.effect.building.IBuildingEffect() {
        };
        return new BuildingCard(nextCardId++, "Building" + nextCardId, era, 2, foodCost, 2, effect, "");
    }
}