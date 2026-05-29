package it.polimi.ingsw.server.model.menu;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerConcurrencyTest {

    @Test
    void testConcurrentGetAvailableGamesAndJoin() throws InterruptedException {
        GameManager gameManager = GameManager.getInstance();
        
        // Setup a game
        Player host = new Player("hostPlayer");
        int gameId = gameManager.createGame(host, 1000);
        Game game = gameManager.getGame(gameId);

        int numThreads = 50;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);
        AtomicInteger exceptions = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    latch.await();
                    // Concurrently alternate between reading the lists and modifying the game
                    if (threadId % 2 == 0) {
                        Player p = new Player("player" + threadId);
                        game.locked(() -> {
                            gameManager.joinGame(gameId, p);
                            return null;
                        });
                    } else {
                        List<Game> available = gameManager.getAvailableGames();
                        assertNotNull(available);
                        
                        Optional<Integer> foundId = gameManager.findGameIdByPlayerNickname("player" + (threadId - 1));
                        // Just checking it doesn't throw ConcurrentModificationException
                    }
                } catch (IllegalStateException e) {
                    if (!e.getMessage().equals("Lobby is full.")) {
                        exceptions.incrementAndGet();
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    exceptions.incrementAndGet();
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        latch.countDown(); // Start all threads simultaneously
        doneLatch.await(5, TimeUnit.SECONDS);

        assertEquals(0, exceptions.get(), "Concurrency exceptions occurred during execution!");
        
        // Cleanup
        gameManager.removeGame(gameId);
        executor.shutdownNow();
    }
}
