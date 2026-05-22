package it.polimi.ingsw.server.model.observer;

import it.polimi.ingsw.server.model.observer.event.IGameEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObserverManager {
    private final List<IGameObserver> observers;

    public ObserverManager() {
        observers = new CopyOnWriteArrayList<>();
    }

    public void addObserver(IGameObserver newObserver) {
        observers.add(newObserver);
    }

    public void removeObserver(IGameObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(IGameEvent event) {
        for (IGameObserver observer : observers)
            try {
                event.dispatch(observer);
            } catch (Exception e) {
                System.out.println("[WARN] Failed to notify observer: " + e.getMessage());
            }
    }

    public void clearObservers() {
        observers.clear();
    }
}
