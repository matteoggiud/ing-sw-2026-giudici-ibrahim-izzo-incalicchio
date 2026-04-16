package it.polimi.ingsw.model.observer;

import java.util.ArrayList;
import java.util.List;

public class ObserverManager {
    private final List<IGameObserver> observers;

    public ObserverManager() {
        observers = new ArrayList<>();
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
