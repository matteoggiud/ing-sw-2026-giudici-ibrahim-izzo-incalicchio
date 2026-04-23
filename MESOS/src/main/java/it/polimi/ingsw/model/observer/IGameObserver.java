package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.observer.event.*;

/**
 * Implemented by VirtualView
 */
public interface IGameObserver {
    /**
     * Called when a game event occurs.
     *
     * @param event the started game event containing relevant information about the game start, such as player nicknames. This method is called by the event dispatch mechanism when a GameStartedEvent is dispatched to this observer.
     */
    default void update(GameStartedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when a game event occurs.
     *
     * @param event the players changed event containing relevant information about the change in players, such as the updated list of player nicknames. This method is called by the event dispatch mechanism when a PlayersChangedEvent is dispatched to this observer.
     */
    default void update(PlayersChangedEvent event) {
        // Default implementation does nothing
    }


}
