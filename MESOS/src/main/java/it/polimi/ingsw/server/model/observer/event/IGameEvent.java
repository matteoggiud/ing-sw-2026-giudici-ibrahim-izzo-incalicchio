package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;

/**
 * Represents a game event that can be observed.
 */
public interface IGameEvent {
    /**
     * Dispatches the event to the given observer, allowing the observer to react to the event.
     *
     * @param observer the observer to which the event should be dispatched
     */
    void dispatch(IGameObserver observer);

}