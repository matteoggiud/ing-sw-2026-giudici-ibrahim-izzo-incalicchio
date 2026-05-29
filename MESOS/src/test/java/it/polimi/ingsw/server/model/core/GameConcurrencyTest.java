package it.polimi.ingsw.server.model.core;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameConcurrencyTest {

    @Test
    void testLockedMutualExclusion() throws InterruptedException {
        // We create a basic game
        Player host = new Player("host");
        Game game = new Game(host, 1);
        
        int numThreads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);
        
        // We use a simple primitive array that is NOT thread-safe on its own
        // If the game.locked() does not provide mutual exclusion, this will suffer from race conditions.
        int[] nonAtomicSharedState = {0};

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    // All threads try to enter the locked block at the exact same time
                    game.locked(() -> {
                        int temp = nonAtomicSharedState[0];
                        // We force a tiny sleep to maximize the chance of a context switch
                        try { Thread.sleep(2); } catch (InterruptedException e) {}
                        nonAtomicSharedState[0] = temp + 1;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        latch.countDown(); // Release the hounds
        doneLatch.await(10, TimeUnit.SECONDS);

        // If the lock worked, the non-atomic state will have exactly 'numThreads' increments without data loss
        assertEquals(numThreads, nonAtomicSharedState[0], "Mutual exclusion failed! Race condition detected inside the locked block.");
        
        executor.shutdownNow();
    }
}
