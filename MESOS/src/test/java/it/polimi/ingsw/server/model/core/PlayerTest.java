package it.polimi.ingsw.server.model.core;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.card.Era;
import it.polimi.ingsw.server.model.card.characters.BuilderCharacterCard;
import it.polimi.ingsw.server.model.card.characters.HunterCharacterCard;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.exceptions.IllegalFoodArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    private int nextCardId;
    private IBuildingEffect effect;

    @BeforeEach
    void setUp() {
        player = new Player("p1");
        nextCardId = 1;
        effect = new IBuildingEffect() {
        };
    }

    @Test
    void getPlayerId() {
        assertNotNull(player.getPlayerId());
        var firstRead = player.getPlayerId();
        var secondRead = player.getPlayerId();
        assertEquals(firstRead, secondRead);
    }

    @Test
    void getNickname() {
        assertEquals("p1", player.getNickname());
    }

    @Test
    void getTotem() {
        assertNull(player.getTotem());
    }

    @Test
    void getPlayerDeck() {
        assertNotNull(player.getPlayerDeck());
        var firstRead = player.getPlayerDeck();
        var secondRead = player.getPlayerDeck();
        assertSame(firstRead, secondRead);
    }

    @Test
    void getPP() {
        assertEquals(0, player.getPP());
    }

    @Test
    void getFood() {
        assertEquals(0, player.getFood());
    }

    @Test
    void addFood() {
        player.addFood(5);
        assertEquals(5, player.getFood());

        player.addFood(-2);
        assertEquals(3, player.getFood());
    }

    @Test
    void addPP() {
        player.addPP(4);
        assertEquals(4, player.getPP());

        player.addPP(-1);
        assertEquals(3, player.getPP());
    }

    @Test
    void canBuyBuildingCard() {
        BuildingCard building = newBuilding(5);
        player.addFood(6);

        player.canBuyBuildingCard(building);

        assertEquals(1, player.getPlayerDeck().getBuildings().size());
        assertSame(building, player.getPlayerDeck().getBuildings().get(0));
        assertEquals(1, player.getFood());
    }

    @Test
    void canBuyBuildingCardWithDiscount() {
        BuildingCard building = newBuilding(2);
        BuilderCharacterCard builder = new BuilderCharacterCard(nextCardId++, "Builder", Era.ERA_1, 2, 0, 4, "");

        player.getPlayerDeck().addCard(builder);
        player.canBuyBuildingCard(building);

        assertEquals(1, player.getPlayerDeck().getCharacters().size());
        assertEquals(1, player.getPlayerDeck().getBuildings().size());
        assertSame(building, player.getPlayerDeck().getBuildings().get(0));
        assertEquals(0, player.getFood());
    }

    @Test
    void canBuyBuildingCardNotEnoughFood() {
        BuildingCard building = newBuilding(5);
        player.addFood(2);

        assertThrows(IllegalFoodArgumentException.class, () -> player.canBuyBuildingCard(building));
        assertTrue(player.getPlayerDeck().getBuildings().isEmpty());
        assertEquals(2, player.getFood());
    }

    @Test
    void addCardsInDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        BuildingCard building = newBuilding(3);
        HunterCharacterCard hunter = new HunterCharacterCard(nextCardId++, "Hunter", Era.ERA_1, 2, false, "");

        cards.add(building);
        cards.add(hunter);

        player.addCardsInDeck(cards);

        assertEquals(1, player.getPlayerDeck().getBuildings().size());
        assertEquals(1, player.getPlayerDeck().getCharacters().size());
        assertSame(building, player.getPlayerDeck().getBuildings().get(0));
        assertSame(hunter, player.getPlayerDeck().getCharacters().get(0));
    }

    @Test
    void setTotem() {
        assertNull(player.getTotem());

        player.setTotem(Totem.BLUE);

        assertEquals(Totem.BLUE, player.getTotem());
    }

    private BuildingCard newBuilding(int foodCost) {
        return new BuildingCard(nextCardId++, "Building" + nextCardId, Era.ERA_1, 2, foodCost, 2, effect, "");
    }
}


