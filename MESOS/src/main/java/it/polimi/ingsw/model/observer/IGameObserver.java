package it.polimi.ingsw.model.observer;

/**
 * Implemented by VirtualView
 */
public interface IGameObserver {
    /**
     * Called when a game event occurs.
     *
     * @param event the game event that occurred
     */
    void update(GameEvent event);
}
