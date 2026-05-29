package it.polimi.ingsw.server.network;

import it.polimi.ingsw.common.network.rmi.server.RemoteClientView;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SessionRegistryConcurrencyTest {

    /**
     * Tests the concurrent registration of multiple sessions with the exact same nickname.
     * <p>
     * 50 threads simulate 50 incoming network connections (e.g. from different devices)
     * attempting to register a session in the SessionRegistry using the identical
     * nickname at exactly the same time.
     * <p>
     * The SessionRegistry must ensure that only 1 session successfully registers for
     * the given nickname, and the other 49 concurrent attempts are safely rejected
     * (e.g. throwing a "Nickname is already connected" RuntimeException).
     * This protects against state corruption where a single player could have
     * multiple active sessions overriding each other.
     */
    @Test
    void testConcurrentSessionRegistration() throws InterruptedException {
        SessionRegistry registry = SessionRegistry.getInstance();
        
        int numThreads = 50;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numThreads);
        AtomicInteger successfulRegistrations = new AtomicInteger(0);
        AtomicInteger rejectedRegistrations = new AtomicInteger(0);

        // We simulate 50 threads trying to register the EXACT SAME NICKNAME at the same time
        // Only one should succeed, the others should get an exception or handle the rejection gracefully
        String targetNickname = "concurrentUser123";

        for (int i = 0; i < numThreads; i++) {
            final int sessionId = i;
            executor.submit(() -> {
                try {
                    latch.await();
                    // Use a dynamic proxy to mock GenericVirtualView to avoid implementing all 20+ methods
                    it.polimi.ingsw.server.network.GenericVirtualView mockView = (it.polimi.ingsw.server.network.GenericVirtualView) java.lang.reflect.Proxy.newProxyInstance(
                            it.polimi.ingsw.server.network.GenericVirtualView.class.getClassLoader(),
                            new Class<?>[]{it.polimi.ingsw.server.network.GenericVirtualView.class},
                            (proxy, method, args) -> {
                                if (method.getName().equals("isReachable")) {
                                    return true;
                                }
                                return null;
                            }
                    );

                    it.polimi.ingsw.server.network.EventHandler mockHandler = new it.polimi.ingsw.server.network.EventHandler(mockView);

                    // Create a mock ClientSession
                    it.polimi.ingsw.server.network.ClientSession mockSession = new it.polimi.ingsw.server.network.ClientSession(mockHandler) {
                        @Override
                        public String getNickname() {
                            return targetNickname;
                        }

                        @Override
                        public Integer getActiveGameId() {
                            return null;
                        }

                        @Override
                        public void clearActiveGameId() { }

                        @Override
                        public void disconnectSession() { }
                    };

                    registry.registerSession(mockSession);
                    successfulRegistrations.incrementAndGet();

                } catch (RuntimeException e) {
                    // Expecting: "Nickname is already connected from another session"
                    rejectedRegistrations.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        latch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);

        // Since it's exactly the same nickname and the mock session is reachable (isAlive() -> true), 
        // the SessionRegistry must allow exactly ONE to register and reject the other 49.
        assertEquals(1, successfulRegistrations.get(), "Only one session should successfully register for a single nickname!");
        assertEquals(numThreads - 1, rejectedRegistrations.get(), "Other sessions must be rejected!");

        // Cleanup
        registry.unregisterSession(registry.getSession(targetNickname));
        executor.shutdownNow();
    }
}
