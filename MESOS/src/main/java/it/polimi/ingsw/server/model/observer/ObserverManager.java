package it.polimi.ingsw.server.model.observer;

import it.polimi.ingsw.server.model.observer.event.IGameEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages the list of observers for a game and provides methods to add, remove, and notify observers of game events.
 */
public class ObserverManager {
    private final List<IGameObserver> observers;

    /**
     * Constructor that initializes the list of observers as a thread-safe CopyOnWriteArrayList.
     */
    public ObserverManager() {
        observers = new CopyOnWriteArrayList<>();
    }

    /**
     * Adds a new observer to the list of observers.
     * @param newObserver the observer to add
     */
    public void addObserver(IGameObserver newObserver) {
        observers.add(newObserver);
    }

    /**
     * Removes an observer from the list of observers.
     * @param observer the observer to remove
     */
    public void removeObserver(IGameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers of a game event by calling the dispatch method of the event for each observer.
     * @param event the game event to dispatch to the observers
     */
    public void notifyObservers(IGameEvent event) {
        for (IGameObserver observer : observers)
            try {
                event.dispatch(observer);
            } catch (Exception e) {
                System.out.println("[WARN] Failed to notify observer: " + e.getMessage());
            }
    }

    /**
     * Clears all observers from the list, effectively removing all registered observers from receiving future notifications.
     */
    public void clearObservers() {
        observers.clear();
    }
}
