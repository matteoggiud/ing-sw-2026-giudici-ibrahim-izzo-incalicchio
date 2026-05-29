package it.polimi.ingsw.client;

import it.polimi.ingsw.common.network.dto.PlayerInfoDTO;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientStateConcurrencyTest {

    @Test
    void testConcurrentReadAndWritePlayersInfo() throws InterruptedException {
        ClientState clientState = new ClientState("tester", 1);
        AtomicInteger exceptionsCaught = new AtomicInteger(0);

        int numReaderThreads = 10;
        int numWriterThreads = 10;
        int totalThreads = numReaderThreads + numWriterThreads;

        ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(totalThreads);

        // Writer threads simulate the network RMI updating the players list frequently
        for (int i = 0; i < numWriterThreads; i++) {
            final int writerId = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < 1000; j++) {
                        List<PlayerInfoDTO> mockInfos = List.of(
                                new PlayerInfoDTO("player" + writerId + "_" + j, 0, 0, "totem", null, null)
                        );
                        clientState.updatePlayersInfo(mockInfos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exceptionsCaught.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        // Reader threads simulate the JavaFX GUI iterating over the list while it gets updated
        for (int i = 0; i < numReaderThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < 1000; j++) {
                        Collection<PlayerInfoDTO> players = clientState.getPlayersInfo();
                        // Iterate through the collection. 
                        // If the map was cleared and mutated underneath, this would throw ConcurrentModificationException
                        for (PlayerInfoDTO p : players) {
                            String name = p.nickname();
                        }
                    }
                } catch (Exception e) {
                    // This will catch ConcurrentModificationException!
                    e.printStackTrace();
                    exceptionsCaught.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // Go!
        doneLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, exceptionsCaught.get(), "ConcurrentModificationException or other exceptions occurred!");
        executor.shutdownNow();
    }
}
