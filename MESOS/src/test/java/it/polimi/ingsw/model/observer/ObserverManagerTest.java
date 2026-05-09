package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.server.model.observer.ObserverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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