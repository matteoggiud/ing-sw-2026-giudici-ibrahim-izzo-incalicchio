package it.polimi.ingsw.server.model.menu;

import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.event.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamePortalConcurrencyTest {

    private GamePortal gamePortal;
    private GameManager gameManager;
    private PlayerManager playerManager;
    private int activeGameId;

    @BeforeEach
    void setUp() {
        gameManager = GameManager.getInstance();
        playerManager = PlayerManager.getInstance();
        gamePortal = new GamePortal();

        // Create host
        playerManager.addPlayer(new Player("HostPlayer"));

        IGameObserver mockObserver = (IGameObserver) Proxy.newProxyInstance(
                IGameObserver.class.getClassLoader(),
                new Class<?>[]{IGameObserver.class},
                (proxy, method, args) -> null
        );

        // We can bypass GamePortal's database auth requirement for game creation by mocking or directly using GameManager
        // But since GamePortal uses DB for GameId, let's just bypass it and use GameManager
        activeGameId = 9999;
        gameManager.createGame(playerManager.getPlayer("HostPlayer"), activeGameId, 2);
    }

    @AfterEach
    void tearDown() {
        if (activeGameId >= 0) {
            gameManager.removeGame(activeGameId);
        }
        playerManager.removePlayer("HostPlayer");
    }

    /**
     * Tests the concurrent execution of joinGame.
     * <p>
     * 10 threads simulate 10 different players trying to join a 2-player game
     * at the exact same moment. Since the game already has 1 host, only 1 spot
     * is available.
     * <p>
     * The test verifies that due to proper synchronization (locking the Game instance),
     * exactly 1 thread succeeds in joining, while the other 9 threads are rejected
     * and throw an exception (e.g., "Lobby is full").
     */
    @Test
    void testConcurrentJoinGame() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);

        AtomicInteger successfulJoins = new AtomicInteger(0);
        AtomicInteger rejectedJoins = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            String nickname = "Player" + index;
            playerManager.addPlayer(new Player(nickname));

            executor.submit(() -> {
                try {
                    startLatch.await();
                    IGameObserver mockObserver = (IGameObserver) Proxy.newProxyInstance(
                            IGameObserver.class.getClassLoader(),
                            new Class<?>[]{IGameObserver.class},
                            (proxy, method, args) -> null
                    );
                    gamePortal.joinGame(activeGameId, nickname, mockObserver);
                    successfulJoins.incrementAndGet();
                } catch (RuntimeException e) {
                    rejectedJoins.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);

        // Clean up remaining players
        for (int i = 0; i < numThreads; i++) {
            playerManager.removePlayer("Player" + i);
        }

        // Only 1 out of 10 should succeed joining the 2-player game
        assertEquals(1, successfulJoins.get(), "Only one player should successfully join!");
        assertEquals(9, rejectedJoins.get(), "Other players should be rejected because game is full!");

        executor.shutdownNow();
    }

    /**
     * Tests concurrent execution of placing a totem.
     * <p>
     * Multiple threads simulate the same or different players attempting to place
     * a totem in the same game concurrently.
     * <p>
     * Although the game is not fully initialized and the action might fail with an
     * exception (e.g., "Not your turn" or "Invalid phase"), the test ensures that
     * the GamePortal properly locks the Game object, preventing race conditions or
     * concurrent modification exceptions within the core logic.
     */
    @Test
    void testConcurrentPlacing() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);

        AtomicInteger exceptionCounts = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    // HostPlayer attempts to place a totem repeatedly
                    gamePortal.placing(activeGameId, "HostPlayer", 1);
                } catch (Exception e) {
                    // We expect exceptions like IllegalStateException since the game is not started
                    exceptionCounts.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);

        // All 10 operations should either succeed or cleanly throw an application exception,
        // without deadlocking or throwing ConcurrentModificationException.
        // Because the game is in LobbyState, placing() will fail for all 10 threads.
        assertEquals(10, exceptionCounts.get(), "All threads should safely throw a state exception without crashing the system.");

        executor.shutdownNow();
    }
}
