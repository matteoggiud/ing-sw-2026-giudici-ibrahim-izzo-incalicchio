package it.polimi.ingsw.server.model.observer;

import it.polimi.ingsw.server.model.observer.event.BoardUpdatedEvent;
import it.polimi.ingsw.server.model.observer.event.GameStartedEvent;
import it.polimi.ingsw.server.model.observer.event.PlayersChangedEvent;

/**
 * Implemented by VirtualView
 */
public interface IGameObserver {
    /**
     * Called when a game started event occurs.
     *
     * @param event the started game event containing relevant information about the game start, such as player nicknames. This method is called by the event dispatch mechanism when a GameStartedEvent is dispatched to this observer.
     */
    default void update(GameStartedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when a player changed event occurs.
     *
     * @param event the players changed event containing relevant information about the change in players, such as the updated list of player nicknames. This method is called by the event dispatch mechanism when a PlayersChangedEvent is dispatched to this observer.
     */
    default void update(PlayersChangedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when a board update event occurs.
     *
     * @param event the board update event containing relevant information about the update to the game board. This method is called by the event dispatch mechanism when a BoardUpdateEvent is dispatched to this observer.
     */
    default void update(BoardUpdatedEvent event){
        // Default implementation does nothing
    }
}
