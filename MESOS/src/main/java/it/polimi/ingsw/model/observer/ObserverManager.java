package it.polimi.ingsw.model.observer;

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

    public void notifyObservers(GameEvent event){
        for (IGameObserver observer : observers)
            observer.update(event);
    }
}
