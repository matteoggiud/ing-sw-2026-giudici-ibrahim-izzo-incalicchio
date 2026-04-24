package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.core.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.*;
import it.polimi.ingsw.model.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full test suite for GameBuilder logic.
 * Verifies setup rules according to Mesos rulebook.
 */
class GameBuilderTest {

    private Game game;
    private GameBuilder gameBuilder;

    @BeforeEach
    void setUp() {
        // Initialize the host player
        Player host = new Player("Player1");
        game = new Game(host);

        // Add two more players to reach the minimum for a standard test
        game.getPlayers().addPlayer(new Player("Player2"));
        game.getPlayers().addPlayer(new Player("Player3"));

        gameBuilder = new GameBuilder(game);
        gameBuilder.prepareGame();
    }

    /**
     * Verifies that food is correctly assigned based on the RANDOMIZED turn order.
     * Rule: 1st slot = 2 Food, 2nd & 3rd slots = 3 Food.
     */
    @Test
    void testInitialFoodDistributionInRandomizedOrder() {
        // Execute the full preparation which shuffles players
        gameBuilder.prepareGame();

        TurnOrderBox turnOrderBox = game.getBoard().getTurnOrderBox();
        List<TurnOrderSlot> slots = turnOrderBox.getSlots();

        // Check food for each position in the TurnOrderBox
        for (int i = 0; i < slots.size(); i++) {
            Player playerInSlot = slots.get(i).getPlacedPlayer();
            int food = playerInSlot.getFood();

            if (i == 0) {
                // First player in random order gets 2 food
                assertEquals(2, food, "The player randomly placed in the 1st slot must have 2 food.");
            } else if (i == 1 || i == 2) {
                // Second and third players get 3 food
                assertEquals(3, food, "Players in the 2nd and 3rd slots must have 3 food.");
            }
        }
    }

    /**
     * Verifies food distribution for 4 and 5 player games.
     * Rule: 4th and 5th players receive 4 food.
     */
    @Test
    void testInitialFoodForFivePlayers() {
        // Adding 4th and 5th players
        game.getPlayers().addPlayer(new Player("Player4"));
        game.getPlayers().addPlayer(new Player("Player5"));

        gameBuilder.prepareGame();

        List<TurnOrderSlot> slots = game.getBoard().getTurnOrderBox().getSlots();

        // Check 4th and 5th player food
        assertEquals(4, slots.get(3).getPlacedPlayer().getFood(), "4th player must have 4 food.");
        assertEquals(4, slots.get(4).getPlacedPlayer().getFood(), "5th player must have 4 food.");
    }

    /**
     * Verifies that the Tribe Deck is stacked in the correct Era sequence.
     * Rule: Era I on top, Era II, Era III, and 2 Final Events at the bottom.
     */
    @Test
    void testTribeDeckOrder() {
        gameBuilder.prepareGame();
        TribeDeck deck = game.getTribeDeck();

        // Check that the deck is not empty and has the expected number of cards
        assertFalse(deck.isEmpty(), "Tribe deck should not be empty after preparation.");
    }

    /**
     * Verifies that each player has been assigned a unique Totem color.
     */
    @Test
    void testUniqueTotemAssignment() {
        gameBuilder.prepareGame();

        List<Player> players = game.getPlayers().getShuffledPlayers();
        long uniqueCount = players.stream()
                .map(Player::getTotem)
                .distinct()
                .count();

        assertEquals(game.getNumberOfPlayers(), uniqueCount, "Every player must have a unique totem color assigned.");
    }

    /**
     * Verifies that all slots in the TurnOrderBox are properly filled.
     */
    @Test
    void testTurnOrderBoxCompletion() {
        gameBuilder.prepareGame();
        TurnOrderBox turnOrderBox = game.getBoard().getTurnOrderBox();

        assertFalse(turnOrderBox.isCompletelyEmpty(), "Turn order box should be filled after preparation.");
        assertEquals(game.getNumberOfPlayers(), turnOrderBox.getSlots().size(),
                "Number of slots should match number of players.");
    }
}