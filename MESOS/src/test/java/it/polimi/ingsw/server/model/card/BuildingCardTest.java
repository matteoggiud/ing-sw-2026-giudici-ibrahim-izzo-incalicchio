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

    @Test
    void getFoodCost() {
        assertEquals(5, buildingCard.getFoodCost());
    }

    @Test
    void getEndGamePP() {
        assertEquals(3, buildingCard.getEndGamePP());
    }

    @Test
    void getEffect() {
        assertSame(effect, buildingCard.getEffect());
    }

    @Test
    void addTo() {
        PlayerDeck deck = new PlayerDeck();

        buildingCard.addTo(deck);

        assertEquals(1, deck.getBuildings().size());
        assertSame(buildingCard, deck.getBuildings().get(0));
    }

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

    @Test
    void validateSelectableFrom() {
        CardRow row = new CardRow();

        assertThrows(IllegalArgumentException.class, () -> buildingCard.validateSelectableFrom(row));

        row.getBuildingRow().add(buildingCard);

        assertDoesNotThrow(() -> buildingCard.validateSelectableFrom(row));
    }

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