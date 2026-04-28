package it.polimi.ingsw.server.model.core;

import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.states.EndedState;
import it.polimi.ingsw.server.model.states.LobbyState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Player hostPlayer;
    private Game game;

    @BeforeEach
    void setUp() {
        hostPlayer = new Player("host");
        int gameId = 1;
        game = new Game(hostPlayer,  gameId);
    }

    @Test
    void getGameId() {
        assertNotNull(game.getGameId());
        assertEquals(game.getGameId(), game.getGameId());
    }

    @Test
    void getHostPlayer() {
        assertSame(hostPlayer, game.getHostPlayer());
    }

    @Test
    void getPlayers() {
        assertNotNull(game.getPlayers());
        assertSame(game.getPlayers(), game.getPlayers());
        assertTrue(game.getPlayers().contains(hostPlayer));
    }

    @Test
    void getBoard() {
        assertNotNull(game.getBoard());
        assertSame(game.getBoard(), game.getBoard());
    }

    @Test
    void getGameState() {
        assertNotNull(game.getGameState());
        assertTrue(game.getGameState() instanceof LobbyState);
    }

    @Test
    void getTribeDeck() {
        assertNotNull(game.getTribeDeck());
        assertSame(game.getTribeDeck(), game.getTribeDeck());
    }

    @Test
    void getBuildingDecks() {
        assertNotNull(game.getBuildingDecks());
        assertSame(game.getBuildingDecks(), game.getBuildingDecks());
    }

    @Test
    void getCurrentEra() {
        assertThrows(IllegalStateException.class, () -> game.getCurrentEra());

        game.setCurrentEra(Era.ERA_1);
        assertEquals(Era.ERA_1, game.getCurrentEra());
    }

    @Test
    void isLastRoundTriggered() {
        assertFalse(game.isLastRoundTriggered());
    }

    @Test
    void getRuleSet() {
        assertNotNull(game.getRuleSet());
        assertSame(game.getRuleSet(), game.getRuleSet());
    }

    @Test
    void getObserverManager() {
        assertNotNull(game.getObserverManager());
        assertSame(game.getObserverManager(), game.getObserverManager());
    }

    @Test
    void getNumberOfPlayers() {
        assertEquals(1, game.getNumberOfPlayers());
    }

    @Test
    void setGameState() {
        EndedState endedState = new EndedState();

        game.setGameState(endedState);
        assertSame(endedState, game.getGameState());

        assertThrows(IllegalArgumentException.class, () -> game.setGameState(null));
    }

    @Test
    void setLastRoundTriggered() {
        game.setLastRoundTriggered(true);
        assertTrue(game.isLastRoundTriggered());

        game.setLastRoundTriggered(false);
        assertFalse(game.isLastRoundTriggered());
    }
}