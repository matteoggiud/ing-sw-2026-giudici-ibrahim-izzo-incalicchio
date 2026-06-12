package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.era.Era2;
import it.polimi.ingsw.server.model.era.Era3;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildingDecksTest {

    private BuildingDecks buildingDecks;
    private int nextCardId;
    private Era era1;
    private Era era2;
    private Era era3;

    @BeforeEach
    void setUp() {
        buildingDecks = new BuildingDecks();
        nextCardId = 1;
        era1 = new Era1();
        era2 = new Era2();
        era3 = new Era3();
    }

    /**
     * Tests that retrieving the building deck for different eras returns distinct, 
     * non-null building decks, and that identical eras return the same deck instance.
     */
    @Test
    void getBuildingDeckReturnsOneDeckForEachEra() {
        BuildingDeck era1Deck = buildingDecks.getBuildingDeck(era1);
        BuildingDeck era2Deck = buildingDecks.getBuildingDeck(era2);
        BuildingDeck era3Deck = buildingDecks.getBuildingDeck(era3);

        assertNotNull(era1Deck);
        assertNotNull(era2Deck);
        assertNotNull(era3Deck);

        assertNotSame(era1Deck, era2Deck);
        assertNotSame(era1Deck, era3Deck);
        assertNotSame(era2Deck, era3Deck);

        assertSame(era1Deck, buildingDecks.getBuildingDeck(new Era1()));
        assertSame(era2Deck, buildingDecks.getBuildingDeck(new Era2()));
        assertSame(era3Deck, buildingDecks.getBuildingDeck(new Era3()));
    }

    /**
     * Tests that attempting to get a building deck with a null era argument throws
     * an IllegalArgumentException.
     */
    @Test
    void getBuildingDeckRejectsNullEra() {
        assertThrows(IllegalArgumentException.class, () -> buildingDecks.getBuildingDeck(null));
    }

    /**
     * Tests that adding separate lists of building cards correctly distributes them 
     * into the corresponding building decks for Era 1, Era 2, and Era 3.
     */
    @Test
    void addBuildingCardsAddsEachListToTheCorrectEraDeck() {
        BuildingCard era1a = newBuilding(era1, 3);
        BuildingCard era1b = newBuilding(era1, 4);
        BuildingCard era2a = newBuilding(era2, 5);
        BuildingCard era3a = newBuilding(era3, 6);

        buildingDecks.addBuildingCards(
                List.of(era1a, era1b),
                List.of(era2a),
                List.of(era3a)
        );

        assertEquals(List.of(era1a, era1b), buildingDecks.getBuildingDeck(era1).getBuildingCards());
        assertEquals(List.of(era2a), buildingDecks.getBuildingDeck(era2).getBuildingCards());
        assertEquals(List.of(era3a), buildingDecks.getBuildingDeck(era3).getBuildingCards());
    }

    /**
     * Tests that providing null lists for any era when adding building cards throws
     * an IllegalArgumentException.
     */
    @Test
    void addBuildingCardsRejectsNullLists() {
        assertThrows(IllegalArgumentException.class,
                () -> buildingDecks.addBuildingCards(null, List.of(), List.of()));

        assertThrows(IllegalArgumentException.class,
                () -> buildingDecks.addBuildingCards(List.of(), null, List.of()));

        assertThrows(IllegalArgumentException.class,
                () -> buildingDecks.addBuildingCards(List.of(), List.of(), null));
    }

    /**
     * Tests that replacing the entire building decks updates the internal era-specific decks
     * accurately based on the provided valid lists of building cards.
     */
    @Test
    void setBuildingDeckSetsTheThreeEraDecks() {
        BuildingCard era1a = newBuilding(era1, 3);
        BuildingCard era2a = newBuilding(era2, 4);
        BuildingCard era3a = newBuilding(era3, 5);
        BuildingCard era3b = newBuilding(era3, 6);

        buildingDecks.setBuildingDeck(
                List.of(era1a),
                List.of(era2a),
                List.of(era3a, era3b)
        );

        assertEquals(List.of(era1a), buildingDecks.getBuildingDeck(era1).getBuildingCards());
        assertEquals(List.of(era2a), buildingDecks.getBuildingDeck(era2).getBuildingCards());
        assertEquals(List.of(era3a, era3b), buildingDecks.getBuildingDeck(era3).getBuildingCards());
    }

    /**
     * Tests that calling setBuildingDeck with any null or empty card lists throws
     * an IllegalArgumentException.
     */
    @Test
    void setBuildingDeckRejectsNullOrEmptyLists() {
        BuildingCard era1a = newBuilding(era1, 3);
        BuildingCard era2a = newBuilding(era2, 4);
        BuildingCard era3a = newBuilding(era3, 5);

        assertThrows(IllegalArgumentException.class,
                () -> buildingDecks.setBuildingDeck(null, List.of(era2a), List.of(era3a)));

        assertThrows(IllegalArgumentException.class,
                () -> buildingDecks.setBuildingDeck(List.of(era1a), null, List.of(era3a)));

        assertThrows(IllegalArgumentException.class,
                () -> buildingDecks.setBuildingDeck(List.of(era1a), List.of(era2a), null));

        assertThrows(IllegalArgumentException.class,
                () -> buildingDecks.setBuildingDeck(List.of(), List.of(era2a), List.of(era3a)));

        assertThrows(IllegalArgumentException.class,
                () -> buildingDecks.setBuildingDeck(List.of(era1a), List.of(), List.of(era3a)));

        assertThrows(IllegalArgumentException.class,
                () -> buildingDecks.setBuildingDeck(List.of(era1a), List.of(era2a), List.of()));
    }

    private BuildingCard newBuilding(Era era, int foodCost) {
        IBuildingEffect effect = new IBuildingEffect() {
        };
        return new BuildingCard(nextCardId++, "Building" + nextCardId, era, 2, foodCost, 2, effect, "");
    }
}
