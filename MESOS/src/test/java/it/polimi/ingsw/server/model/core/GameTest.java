package it.polimi.ingsw.server.model.core;

import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.states.EndedState;
import it.polimi.ingsw.server.model.states.LobbyState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Player hostPlayer;
    private Game game;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        hostPlayer = new Player("host");
        int gameId = 1;
        game = new Game(hostPlayer,  gameId);
        era1 = new Era1();
    }

    /**
     * Tests getting the game ID.
     * Validates that {@link Game#getGameId()} returns the initialized game ID.
     */
    @Test
    void getGameId() {
        assertNotNull(game.getGameId());
        assertEquals(game.getGameId(), game.getGameId());
    }

    /**
     * Tests getting the host player.
     * Validates that {@link Game#getHostPlayer()} returns the correct host player reference.
     */
    @Test
    void getHostPlayer() {
        assertSame(hostPlayer, game.getHostPlayer());
    }

    /**
     * Tests getting the list of players.
     * Validates that {@link Game#getPlayers()} correctly retrieves the players and contains the host.
     */
    @Test
    void getPlayers() {
        assertNotNull(game.getPlayers());
        assertSame(game.getPlayers(), game.getPlayers());
        assertTrue(game.getPlayers().contains(hostPlayer));
    }

    /**
     * Tests getting the game board.
     * Validates that {@link Game#getBoard()} returns the initialized board object.
     */
    @Test
    void getBoard() {
        assertNotNull(game.getBoard());
        assertSame(game.getBoard(), game.getBoard());
    }

    /**
     * Tests getting the game state.
     * Validates that {@link Game#getGameState()} returns the initial state which is LobbyState.
     */
    @Test
    void getGameState() {
        assertNotNull(game.getGameState());
        assertTrue(game.getGameState() instanceof LobbyState);
    }

    /**
     * Tests getting the tribe deck.
     * Validates that {@link Game#getTribeDeck()} returns the initial tribe deck object.
     */
    @Test
    void getTribeDeck() {
        assertNotNull(game.getTribeDeck());
        assertSame(game.getTribeDeck(), game.getTribeDeck());
    }

    /**
     * Tests getting the building decks.
     * Validates that {@link Game#getBuildingDecks()} returns the valid collection of building decks.
     */
    @Test
    void getBuildingDecks() {
        assertNotNull(game.getBuildingDecks());
        assertSame(game.getBuildingDecks(), game.getBuildingDecks());
    }

    /**
     * Tests getting the current era of the game.
     * Validates that {@link Game#getCurrentEra()} throws an exception before initialization, and returns the correct era afterwards.
     */
    @Test
    void getCurrentEra() {
        assertThrows(IllegalStateException.class, () -> game.getCurrentEra());

        game.setCurrentEra(era1);
        assertSame(era1, game.getCurrentEra());
    }

    /**
     * Tests whether the last round triggered flag is set.
     * Validates that {@link Game#isLastRoundTriggered()} is false by default.
     */
    @Test
    void isLastRoundTriggered() {
        assertFalse(game.isLastRoundTriggered());
    }

    /**
     * Tests getting the rule set associated with the game.
     * Validates that {@link Game#getRuleSet()} returns the initialized rule set.
     */
    @Test
    void getRuleSet() {
        assertNotNull(game.getRuleSet());
        assertSame(game.getRuleSet(), game.getRuleSet());
    }

    /**
     * Tests getting the observer manager for the game.
     * Validates that {@link Game#getObserverManager()} returns a valid observer manager instance.
     */
    @Test
    void getObserverManager() {
        assertNotNull(game.getObserverManager());
        assertSame(game.getObserverManager(), game.getObserverManager());
    }

    /**
     * Tests getting the current number of players.
     * Validates that {@link Game#getNumberOfPlayers()} accurately reflects the total participant count.
     */
    @Test
    void getNumberOfPlayers() {
        assertEquals(1, game.getNumberOfPlayers());
    }

    /**
     * Tests setting the game state.
     * Validates that {@link Game#setGameState(it.polimi.ingsw.server.model.states.State)} correctly updates the current state and throws an exception on null.
     */
    @Test
    void setGameState() {
        EndedState endedState = new EndedState(Map.of(hostPlayer, 0));

        game.setGameState(endedState);
        assertSame(endedState, game.getGameState());

        assertThrows(IllegalArgumentException.class, () -> game.setGameState(null));
    }


    /**
     * Tests setting the last round triggered flag.
     * Validates that {@link Game#setLastRoundTriggered(boolean)} sets the correct boolean state.
     */
    @Test
    void setLastRoundTriggered() {
        game.setLastRoundTriggered(true);
        assertTrue(game.isLastRoundTriggered());

        game.setLastRoundTriggered(false);
        assertFalse(game.isLastRoundTriggered());
    }
}