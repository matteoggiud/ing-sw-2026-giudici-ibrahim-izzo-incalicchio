package it.polimi.ingsw.model.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ObserverManagerTest {

    private ObserverManager manager;

    @BeforeEach
    void setUp() {
        manager = new ObserverManager();
    }

    @Test
    void notifyObservers_deliversEventToAllRegistered() {
        AtomicBoolean called1 = new AtomicBoolean(false);
        AtomicBoolean called2 = new AtomicBoolean(false);
        AtomicReference<GameEvent> received = new AtomicReference<>();

        manager.addObserver(event -> {
            called1.set(true);
            received.set(event);
        });

        manager.addObserver(event -> called2.set(true));

        UUID gameId = UUID.randomUUID();
        GameEvent event = new GameEvent(GameEventType.TOTEM_PLACED, gameId, null, "test");

        manager.notifyObservers(event);

        assertTrue(called1.get(), "Primo observer non è stato chiamato");
        assertTrue(called2.get(), "Secondo observer non è stato chiamato");
        assertNotNull(received.get(), "Nessun evento ricevuto dal primo observer");
        assertEquals(GameEventType.TOTEM_PLACED, received.get().type());
        assertEquals(gameId, received.get().gameId());
    }

}