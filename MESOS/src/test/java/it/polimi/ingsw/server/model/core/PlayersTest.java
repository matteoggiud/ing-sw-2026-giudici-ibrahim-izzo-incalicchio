package it.polimi.ingsw.server.model.core;

import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayersTest {

    private Players players;
    private Player p1;
    private Player p2;
    private Player p3;

    @BeforeEach
    void setUp() {
        players = new Players();
        p1 = new Player("p1");
        p2 = new Player("p2");
        p3 = new Player("p3");
    }

    @Test
    void addPlayer() {
        assertTrue(players.isEmpty());

        players.addPlayer(p1);
        assertEquals(1, players.size());
        assertTrue(players.contains(p1));

        assertThrows(IllegalArgumentException.class, () -> players.addPlayer(null));
        assertThrows(IllegalArgumentException.class, () -> players.addPlayer(p1));

        players.addPlayer(p2);
        players.addPlayer(p3);
        players.addPlayer(new Player("p4"));
        players.addPlayer(new Player("p5"));

        assertTrue(players.isFull());
        assertThrows(IllegalStateException.class, () -> players.addPlayer(new Player("overflow")));
    }

    @Test
    void getHost() {
        assertThrows(IllegalStateException.class, () -> players.getHost());

        players.addPlayer(p1);
        players.addPlayer(p2);

        assertSame(p1, players.getHost());
    }

    @Test
    void removePlayer() {
        players.addPlayer(p1);
        players.addPlayer(p2);
        players.addPlayer(p3);

        players.removePlayer(p2);
        assertEquals(2, players.size());
        assertFalse(players.contains(p2));
        assertTrue(players.contains(p1));
        assertTrue(players.contains(p3));

        assertThrows(IllegalArgumentException.class, () -> players.removePlayer(null));
        assertThrows(IllegalArgumentException.class, () -> players.removePlayer(new Player("not-present")));
        assertThrows(IllegalArgumentException.class, () -> players.removePlayer(p1));
        assertEquals(2, players.size());
        assertSame(p1, players.getHost());
    }

    @Test
    void size() {
        assertEquals(0, players.size());

        players.addPlayer(p1);
        players.addPlayer(p2);
        assertEquals(2, players.size());

        players.removePlayer(p2);
        assertEquals(1, players.size());
    }

    @Test
    void getPlayers() {
        players.addPlayer(p1);
        players.addPlayer(p2);

        List<Player> snapshot = players.getPlayers();

        assertEquals(2, snapshot.size());
        assertTrue(snapshot.contains(p1));
        assertTrue(snapshot.contains(p2));

        snapshot.clear();
        assertEquals(2, players.size());
    }

    @Test
    void isEmpty() {
        assertTrue(players.isEmpty());

        players.addPlayer(p1);
        assertFalse(players.isEmpty());

        players.addPlayer(p2);
        players.removePlayer(p2);
        assertFalse(players.isEmpty());
        assertSame(p1, players.getHost());
    }

    @Test
    void isFull() {
        assertFalse(players.isFull());

        players.addPlayer(p1);
        players.addPlayer(p2);
        players.addPlayer(p3);
        players.addPlayer(new Player("p4"));
        assertFalse(players.isFull());

        players.addPlayer(new Player("p5"));
        assertTrue(players.isFull());
        assertEquals(StandardRuleSet.MAX_PLAYERS, players.size());
    }

    @Test
    void contains() {
        assertFalse(players.contains(p1));

        players.addPlayer(p1);
        assertTrue(players.contains(p1));
        assertFalse(players.contains(p2));
    }

    @Test
    void getPlayerNicknames() {
        players.addPlayer(p1);
        players.addPlayer(p2);
        players.addPlayer(p3);

        List<String> nicknames = players.getPlayerNicknames();

        assertEquals(3, nicknames.size());
        assertEquals(List.of("p1", "p2", "p3"), nicknames);
    }

    @Test
    void getShuffledPlayers() {
        players.addPlayer(p1);
        players.addPlayer(p2);
        players.addPlayer(p3);

        List<Player> shuffled = players.getShuffledPlayers();

        assertEquals(3, shuffled.size());
        assertEquals(Set.of(p1, p2, p3), new HashSet<>(shuffled));

        shuffled.clear();
        assertEquals(3, players.size());
    }
}
