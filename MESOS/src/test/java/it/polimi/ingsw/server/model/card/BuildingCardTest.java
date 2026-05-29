package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.card.characters.BuilderCharacterCard;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.exceptions.IllegalFoodArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingCardTest {

    private int nextCardId;
    private IBuildingEffect effect;
    private BuildingCard buildingCard;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        nextCardId = 1;
        effect = new IBuildingEffect() {
        };
        era1 = new Era1();
        buildingCard = new BuildingCard(nextCardId++, "BuildingA", era1, 2, 5, 3, effect,"");
    }

    /**
     * Tests getting the food cost of a building card.
     * Validates that {@link BuildingCard#getFoodCost()} returns the correct cost.
     */
    @Test
    void getFoodCost() {
        assertEquals(5, buildingCard.getFoodCost());
    }

    /**
     * Tests getting the end game prestige points provided by a building card.
     * Validates that {@link BuildingCard#getEndGamePP()} returns the correct value.
     */
    @Test
    void getEndGamePP() {
        assertEquals(3, buildingCard.getEndGamePP());
    }

    /**
     * Tests retrieving the building's effect.
     * Validates that {@link BuildingCard#getEffect()} returns the assigned effect object.
     */
    @Test
    void getEffect() {
        assertSame(effect, buildingCard.getEffect());
    }

    /**
     * Tests adding a building card to a player's deck.
     * Validates that {@link BuildingCard#addTo(PlayerDeck)} correctly places it in the buildings list.
     */
    @Test
    void addTo() {
        PlayerDeck deck = new PlayerDeck();

        buildingCard.addTo(deck);

        assertEquals(1, deck.getBuildings().size());
        assertSame(buildingCard, deck.getBuildings().get(0));
    }

    /**
     * Tests whether a building card can be selected from a card row by a player.
     * Validates that {@link BuildingCard#canBeSelectedFrom(CardRow, Player)} correctly accounts for presence in the row and player's food, including any builder discounts.
     */
    @Test
    void canBeSelectedFrom() {
        CardRow row = new CardRow();
        Player player = new Player("p1");

        assertFalse(buildingCard.canBeSelectedFrom(row, player));

        row.getBuildingRow().add(buildingCard);
        assertFalse(buildingCard.canBeSelectedFrom(row, player));

        player.addFood(5);
        assertTrue(buildingCard.canBeSelectedFrom(row, player));

        Player discountedPlayer = new Player("p2");
        discountedPlayer.addFood(3);
        discountedPlayer.getPlayerDeck().addCard(new BuilderCharacterCard(nextCardId++, "Builder", era1, 2, 0, 2, ""));
        assertTrue(buildingCard.canBeSelectedFrom(row, discountedPlayer));
    }

    /**
     * Tests validation logic for selecting a building card.
     * Validates that {@link BuildingCard#validateSelectableFrom(CardRow)} throws an exception if the card is not present in the row.
     */
    @Test
    void validateSelectableFrom() {
        CardRow row = new CardRow();

        assertThrows(IllegalArgumentException.class, () -> buildingCard.validateSelectableFrom(row));

        row.getBuildingRow().add(buildingCard);

        assertDoesNotThrow(() -> buildingCard.validateSelectableFrom(row));
    }

    /**
     * Tests resolving the acquisition of a building card from the offer row.
     * Validates that it is removed from the row, added to the player's deck, and that the player's food is correctly decremented, throwing if insufficient.
     */
    @Test
    void resolveFromOfferRow() {
        CardRow row = new CardRow();
        Player player = new Player("p1");
        player.addFood(6);
        row.getBuildingRow().add(buildingCard);

        buildingCard.resolveFromOfferRow(player, row);

        assertFalse(row.getBuildingRow().getBuildingCards().contains(buildingCard));
        assertEquals(1, player.getPlayerDeck().getBuildings().size());
        assertSame(buildingCard, player.getPlayerDeck().getBuildings().get(0));
        assertEquals(1, player.getFood());

        BuildingCard expensive = new BuildingCard(nextCardId++, "TooExpensive", era1, 2, 9, 1, effect,"");
        row.getBuildingRow().add(expensive);
        Player poorPlayer = new Player("p2");
        poorPlayer.addFood(2);

        assertThrows(IllegalFoodArgumentException.class, () -> expensive.resolveFromOfferRow(poorPlayer, row));
        assertTrue(row.getBuildingRow().getBuildingCards().contains(expensive));
        assertTrue(poorPlayer.getPlayerDeck().getBuildings().isEmpty());
    }
}