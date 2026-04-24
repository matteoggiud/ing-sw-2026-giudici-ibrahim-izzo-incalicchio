package it.polimi.ingsw.server.model.observer;

import it.polimi.ingsw.server.model.observer.event.*;

/**
 * Observer interface implemented by remote or local views.
 * Each event type has its own dedicated update method.
 */
public interface IGameObserver {

    /**
     * Called when a game started event occurs.
     *
     * @param event the event containing information about the started game
     */
    default void update(GameStartedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when the list of players in the lobby changes.
     *
     * @param event the event containing the updated player list
     */
    default void update(PlayersChangedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when the board state changes.
     *
     * @param event the event containing updated board information
     */
    default void update(BoardUpdatedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when the player whose turn it is to place a totem changes.
     *
     * @param event the event containing the current player for the placing totem phase
     */
    default void update(PlaceTotemTurnChangedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when the player whose turn it is to resolve an action changes.
     *
     * @param event the event containing the current player for the action resolution phase
     */
    default void update(ResolveActionTurnChangedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when the player whose turn it is to resolve an extra action changes.
     *
     * @param event the event containing the current player for the extra action phase
     */
    default void update(ExtraActionTurnChangedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when a player's resources change.
     *
     * @param event the event containing the updated resources of one player
     */
    default void update(PlayerResourcesUpdatedEvent event) {
        // Default implementation does nothing
    }

    /**
     * Called when the game ends and the final ranking becomes available.
     *
     * @param event the event containing the final ranking
     */
    default void update(GameEndedEvent event) {
        // Default implementation does nothing
    }
}
