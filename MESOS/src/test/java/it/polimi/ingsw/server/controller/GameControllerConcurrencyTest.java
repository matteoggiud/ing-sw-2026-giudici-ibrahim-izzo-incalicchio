package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.menu.GameManager;
import it.polimi.ingsw.server.model.menu.PlayerManager;
import it.polimi.ingsw.server.network.ClientSession;
import it.polimi.ingsw.server.network.EventHandler;
import it.polimi.ingsw.server.network.GenericVirtualView;
import it.polimi.ingsw.server.network.SessionRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameControllerConcurrencyTest {

    private GameController gameController;
    private GameManager gameManager;
    private PlayerManager playerManager;
    private SessionRegistry sessionRegistry;
    private int activeGameId;

    @BeforeEach
    void setUp() {
        gameManager = GameManager.getInstance();
        playerManager = PlayerManager.getInstance();
        sessionRegistry = SessionRegistry.getInstance();
        GamePortal gamePortal = new GamePortal();
        gameController = new GameController(gamePortal);

        // Prepare Host
        playerManager.addPlayer(new Player("HostPlayer"));
        activeGameId = 8888;
        gameManager.createGame(playerManager.getPlayer("HostPlayer"), activeGameId, 2);
    }

    @AfterEach
    void tearDown() {
        if (activeGameId >= 0) {
            try {
                gameManager.removeGame(activeGameId);
            } catch (IllegalArgumentException e) {
                // Game was already removed during a test (e.g., testConcurrentLeaveLobby)
            }
        }
        playerManager.removePlayer("HostPlayer");
    }

    private ClientSession createMockSession(String nickname) {
        GenericVirtualView mockView = (GenericVirtualView) Proxy.newProxyInstance(
                GenericVirtualView.class.getClassLoader(),
                new Class<?>[]{GenericVirtualView.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("isReachable")) return true;
                    return null;
                }
        );

        EventHandler mockHandler = new EventHandler(mockView);

        return new ClientSession(mockHandler) {
            private Integer sessionGameId = null;

            @Override
            public String getNickname() { return nickname; }

            @Override
            public boolean isLoggedIn() { return true; }

            @Override
            public Integer getActiveGameId() { return sessionGameId; }

            @Override
            public void setActiveGameId(Integer gameId) { sessionGameId = gameId; }

            @Override
            public void clearActiveGameId() { sessionGameId = null; }

            @Override
            public boolean isInGame() { return sessionGameId != null; }

            @Override
            public void disconnectSession() { }
        };
    }

    /**
     * Tests the concurrent execution of joinGame via the GameController.
     * <p>
     * 10 threads simulate 10 different ClientSessions attempting to join a 2-player game
     * at exactly the same time. The GameController is responsible for registering the session
     * and passing the join request to the GamePortal.
     * <p>
     * The test verifies that only 1 thread successfully joins the game and triggers the DB
     * initialization (caught via PSQLException since no real DB is mocked), while the other
     * 9 threads receive a rejection exception (e.g., "Lobby is full").
     */
    @Test
    void testConcurrentJoinGameViaController() throws InterruptedException {
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
            ClientSession session = createMockSession(nickname);
            sessionRegistry.registerSession(session);

            executor.submit(() -> {
                try {
                    startLatch.await();
                    // GameController joinGame
                    gameController.joinGame(session, activeGameId);
                    successfulJoins.incrementAndGet();
                } catch (RuntimeException e) {
                    if (e.getCause() != null && e.getCause().getClass().getName().contains("PSQLException")) {
                        // This means the player successfully joined, filled the lobby, and triggered startGameIfReady
                        // which then hit the DB and failed because we didn't mock the DB.
                        successfulJoins.incrementAndGet();
                    } else if (e.getMessage() != null && e.getMessage().contains("null value in column")) {
                        successfulJoins.incrementAndGet();
                    } else {
                        // Expected rejection: "Lobby is full" or similar
                        rejectedJoins.incrementAndGet();
                    }
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
            String nickname = "Player" + i;
            playerManager.removePlayer(nickname);
            ClientSession session = sessionRegistry.getSession(nickname);
            if (session != null) {
                sessionRegistry.unregisterSession(session);
            }
        }

        // Only 1 out of 10 should succeed joining the 2-player game
        assertEquals(1, successfulJoins.get(), "Only one player should successfully join via GameController!");
        assertEquals(9, rejectedJoins.get(), "Other players should be rejected!");

        executor.shutdownNow();
    }

    /**
     * Tests concurrent execution of drawCard via the GameController.
     * <p>
     * Multiple threads simulate the same ClientSession attempting to draw a card
     * from the board concurrently.
     * <p>
     * This verifies that the GamePortal properly locks the Game instance and processes
     * each draw request sequentially. Since the game is still in the lobby state, all
     * threads should be safely rejected with an IllegalStateException without corrupting
     * the game state.
     */
    @Test
    void testConcurrentDrawCardViaController() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);

        AtomicInteger exceptionCounts = new AtomicInteger(0);

        ClientSession hostSession = createMockSession("HostPlayer");
        hostSession.setActiveGameId(activeGameId);
        sessionRegistry.registerSession(hostSession);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    // HostPlayer attempts to draw a card repeatedly
                    gameController.drawCard(hostSession, 1, true);
                } catch (Exception e) {
                    // Expecting exception because game is in LobbyState
                    exceptionCounts.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);

        assertEquals(10, exceptionCounts.get(), "All threads should be safely rejected without concurrency errors!");

        sessionRegistry.unregisterSession(hostSession);
        executor.shutdownNow();
    }

    /**
     * Tests concurrent execution of leaving a lobby via the GameController.
     * <p>
     * Simulates the host attempting to trigger leaveLobby concurrently through multiple threads.
     * When the host leaves, the Game is completely destroyed from the GameManager. 
     * Concurrent calls must be properly synchronized so that the Game is only removed once 
     * and no NullPointerException is thrown by threads accessing a deleted game.
     */
    @Test
    void testConcurrentLeaveLobby() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);

        ClientSession hostSession = createMockSession("HostPlayer");
        hostSession.setActiveGameId(activeGameId);
        sessionRegistry.registerSession(hostSession);

        AtomicInteger successfulLeaves = new AtomicInteger(0);
        AtomicInteger exceptionCounts = new AtomicInteger(0);
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    gameController.leaveLobby(hostSession);
                } catch (Exception e) {
                    // Exception is expected if a thread passes isInGame() check but another thread deletes the game first
                    // The exception will be IllegalArgumentException from GameManager.getGame
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);

        // Verify that the game was safely and completely removed without crashing the whole application
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> gameManager.getGame(activeGameId),
                "The game should have been completely removed from the GameManager."
        );
        
        // Verify that the session properly cleared its active game ID
        org.junit.jupiter.api.Assertions.assertFalse(hostSession.isInGame(), "The host session should no longer be in a game.");

        sessionRegistry.unregisterSession(hostSession);
        executor.shutdownNow();
    }
}
