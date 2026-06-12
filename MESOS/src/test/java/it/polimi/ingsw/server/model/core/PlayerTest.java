package it.polimi.ingsw.server.model.core;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
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
    private Era1 era1;

    @BeforeEach
    void setUp() {
        player = new Player("p1");
        nextCardId = 1;
        effect = new IBuildingEffect() {
        };
        era1 = new Era1();
    }

    /**
     * Tests retrieving the player ID.
     * Validates that {@link Player#getPlayerId()} correctly returns the unique identifier for the player and that it is consistent across calls.
     */
    @Test
    void getPlayerId() {
        assertNotNull(player.getPlayerId());
        var firstRead = player.getPlayerId();
        var secondRead = player.getPlayerId();
        assertEquals(firstRead, secondRead);
    }

    /**
     * Tests retrieving the player's nickname.
     * Validates that {@link Player#getNickname()} correctly returns the initialized nickname.
     */
    @Test
    void getNickname() {
        assertEquals("p1", player.getNickname());
    }

    /**
     * Tests retrieving the player's totem.
     * Validates that {@link Player#getTotem()} returns null before any totem is assigned.
     */
    @Test
    void getTotem() {
        assertNull(player.getTotem());
    }

    /**
     * Tests retrieving the player's deck.
     * Validates that {@link Player#getPlayerDeck()} returns a valid and consistent deck instance.
     */
    @Test
    void getPlayerDeck() {
        assertNotNull(player.getPlayerDeck());
        var firstRead = player.getPlayerDeck();
        var secondRead = player.getPlayerDeck();
        assertSame(firstRead, secondRead);
    }

    /**
     * Tests retrieving the player's prestige points.
     * Validates that {@link Player#getPP()} defaults to 0.
     */
    @Test
    void getPP() {
        assertEquals(0, player.getPP());
    }

    /**
     * Tests retrieving the player's food tokens.
     * Validates that {@link Player#getFood()} defaults to 0.
     */
    @Test
    void getFood() {
        assertEquals(0, player.getFood());
    }

    /**
     * Tests adding food to the player.
     * Validates that {@link Player#addFood(int)} updates the food token count properly, including deductions.
     */
    @Test
    void addFood() {
        player.addFood(5);
        assertEquals(5, player.getFood());

        player.addFood(-2);
        assertEquals(3, player.getFood());
    }

    /**
     * Tests adding prestige points to the player.
     * Validates that {@link Player#addPP(int)} updates the prestige points count properly, including deductions.
     */
    @Test
    void addPP() {
        player.addPP(4);
        assertEquals(4, player.getPP());

        player.addPP(-1);
        assertEquals(3, player.getPP());
    }

    /**
     * Tests buying a building card when the player has enough food.
     * Validates that {@link Player#canBuyBuildingCard(BuildingCard)} adds the building to the player's deck and deducts food accordingly.
     */
    @Test
    void canBuyBuildingCard() {
        BuildingCard building = newBuilding(5);
        player.addFood(6);

        player.canBuyBuildingCard(building);

        assertEquals(1, player.getPlayerDeck().getBuildings().size());
        assertSame(building, player.getPlayerDeck().getBuildings().get(0));
        assertEquals(1, player.getFood());
    }

    /**
     * Tests buying a building card with an active builder character discount.
     * Validates that {@link Player#canBuyBuildingCard(BuildingCard)} applies the discount provided by the builder, allowing the purchase with less food.
     */
    @Test
    void canBuyBuildingCardWithDiscount() {
        BuildingCard building = newBuilding(2);
        BuilderCharacterCard builder = new BuilderCharacterCard(nextCardId++, "Builder", era1, 2, 0, 4, "");

        player.getPlayerDeck().addCard(builder);
        player.canBuyBuildingCard(building);

        assertEquals(1, player.getPlayerDeck().getCharacters().size());
        assertEquals(1, player.getPlayerDeck().getBuildings().size());
        assertSame(building, player.getPlayerDeck().getBuildings().get(0));
        assertEquals(0, player.getFood());
    }

    /**
     * Tests attempting to buy a building card without sufficient food.
     * Validates that {@link Player#canBuyBuildingCard(BuildingCard)} throws an exception and does not change player's state.
     */
    @Test
    void canBuyBuildingCardNotEnoughFood() {
        BuildingCard building = newBuilding(5);
        player.addFood(2);

        assertThrows(IllegalFoodArgumentException.class, () -> player.canBuyBuildingCard(building));
        assertTrue(player.getPlayerDeck().getBuildings().isEmpty());
        assertEquals(2, player.getFood());
    }

    /**
     * Tests adding a generic list of cards to the player's deck.
     * Validates that {@link Player#addCardsInDeck(List)} places each card into the appropriate subclass-specific list within the deck.
     */
    @Test
    void addCardsInDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        BuildingCard building = newBuilding(3);
        HunterCharacterCard hunter = new HunterCharacterCard(nextCardId++, "Hunter", era1, 2, false, "");

        cards.add(building);
        cards.add(hunter);

        player.addCardsInDeck(cards);

        assertEquals(1, player.getPlayerDeck().getBuildings().size());
        assertEquals(1, player.getPlayerDeck().getCharacters().size());
        assertSame(building, player.getPlayerDeck().getBuildings().get(0));
        assertSame(hunter, player.getPlayerDeck().getCharacters().get(0));
    }

    /**
     * Tests setting the player's totem.
     * Validates that {@link Player#setTotem(Totem)} correctly assigns the totem to the player.
     */
    @Test
    void setTotem() {
        assertNull(player.getTotem());

        player.setTotem(Totem.BLUE);

        assertEquals(Totem.BLUE, player.getTotem());
    }

    private BuildingCard newBuilding(int foodCost) {
        return new BuildingCard(nextCardId++, "Building" + nextCardId, era1, 2, foodCost, 2, effect, "");
    }
}


