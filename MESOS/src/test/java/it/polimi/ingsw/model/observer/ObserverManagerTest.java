package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.observer.event.IGameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class ObserverManagerTest {

    private ObserverManager manager;

    @BeforeEach
    void setUp() {
        manager = new ObserverManager();
    }

    @Test
    void notifyObservers_deliversEventToAllRegistered() {


    }

}