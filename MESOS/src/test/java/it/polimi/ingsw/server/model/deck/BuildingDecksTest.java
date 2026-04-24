package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.BuildingCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildingDecksTest {

    private BuildingDecks buildingDecks;
    private int nextCardId;

    @BeforeEach
    void setUp() {
        buildingDecks = new BuildingDecks();
        nextCardId = 1;
    }

    @Test
    void getBuildingDeck() {
        BuildingDeck era1Deck = buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_1);
        BuildingDeck era2Deck = buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_2);
        BuildingDeck era3Deck = buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_3);

        assertNotNull(era1Deck);
        assertNotNull(era2Deck);
        assertNotNull(era3Deck);
        assertTrue(era1Deck.getBuildingCards().isEmpty());
        assertTrue(era2Deck.getBuildingCards().isEmpty());
        assertTrue(era3Deck.getBuildingCards().isEmpty());
        assertSame(era1Deck, buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_1));
    }

    @Test
    void addBuildingCards() {
        BuildingCard era1a = newBuilding(it.polimi.ingsw.server.model.card.Era.ERA_1, 3);
        BuildingCard era1b = newBuilding(it.polimi.ingsw.server.model.card.Era.ERA_1, 4);
        BuildingCard era2 = newBuilding(it.polimi.ingsw.server.model.card.Era.ERA_2, 5);
        BuildingCard era3 = newBuilding(it.polimi.ingsw.server.model.card.Era.ERA_3, 6);

        List<BuildingCard> cards = List.of(era1a, era1b, era2, era3);
        buildingDecks.addBuildingCards(cards);

        assertEquals(2, buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_1).getBuildingCards().size());
        assertEquals(1, buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_2).getBuildingCards().size());
        assertEquals(1, buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_3).getBuildingCards().size());
        assertTrue(buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_1).getBuildingCards().contains(era1a));
        assertTrue(buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_1).getBuildingCards().contains(era1b));
        assertTrue(buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_2).getBuildingCards().contains(era2));
        assertTrue(buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_3).getBuildingCards().contains(era3));

        assertThrows(IllegalArgumentException.class, () -> buildingDecks.addBuildingCards(null));
    }

    @Test
    void setBuildingDeck() {
        BuildingCard oldEra1 = newBuilding(it.polimi.ingsw.server.model.card.Era.ERA_1, 3);
        BuildingCard oldEra2 = newBuilding(it.polimi.ingsw.server.model.card.Era.ERA_2, 4);
        buildingDecks.addBuildingCards(List.of(oldEra1, oldEra2));

        BuildingCard newEra1 = newBuilding(it.polimi.ingsw.server.model.card.Era.ERA_1, 5);
        BuildingCard newEra3a = newBuilding(it.polimi.ingsw.server.model.card.Era.ERA_3, 6);
        BuildingCard newEra3b = newBuilding(it.polimi.ingsw.server.model.card.Era.ERA_3, 7);
        ArrayList<BuildingCard> replacement = new ArrayList<>(List.of(newEra1, newEra3a, newEra3b));

        buildingDecks.setBuildingDeck(replacement);

        assertEquals(1, buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_1).getBuildingCards().size());
        assertEquals(0, buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_2).getBuildingCards().size());
        assertEquals(2, buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_3).getBuildingCards().size());
        assertTrue(buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_1).getBuildingCards().contains(newEra1));
        assertTrue(buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_3).getBuildingCards().contains(newEra3a));
        assertTrue(buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_3).getBuildingCards().contains(newEra3b));
        assertFalse(buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_1).getBuildingCards().contains(oldEra1));
        assertFalse(buildingDecks.getBuildingDeck(it.polimi.ingsw.server.model.card.Era.ERA_2).getBuildingCards().contains(oldEra2));

        assertThrows(IllegalArgumentException.class, () -> buildingDecks.setBuildingDeck(null));
        assertThrows(IllegalArgumentException.class, () -> buildingDecks.setBuildingDeck(List.of()));
    }

    private BuildingCard newBuilding(it.polimi.ingsw.server.model.card.Era era, int foodCost) {
        it.polimi.ingsw.server.model.effect.building.IBuildingEffect effect = new it.polimi.ingsw.server.model.effect.building.IBuildingEffect() {
        };
        return new BuildingCard(nextCardId++, "Building" + nextCardId, era, 2, foodCost, 2, effect);
    }
}