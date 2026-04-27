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
    void update(GameStartedEvent event);

    /**
     * Called when the list of players in the lobby changes.
     *
     * @param event the event containing the updated player list
     */
    void update(PlayersChangedEvent event);

    /**
     * Called when the board state changes.
     *
     * @param event the event containing updated board information
     */

    void update(BoardUpdatedEvent event);

    /**
     * Called when the game transitions to a new era.
     *
     * @param event the event containing the new era
     */
    void update(EraChangedEvent event);

    /**
     * Called when the player whose turn it is to place a totem changes.
     *
     * @param event the event containing the current player for the placing totem phase
     */
    void update(PlaceTotemTurnChangedEvent event);

    /**
     * Called when the player whose turn it is to resolve an action changes.
     *
     * @param event the event containing the current player for the action resolution phase
     */
    void update(ResolveActionTurnChangedEvent event);

    /**
     * Called when the player whose turn it is to resolve an extra action changes.
     *
     * @param event the event containing the current player for the extra action phase
     */
    void update(ExtraActionTurnChangedEvent event);

    /**
     * Called when a player's resources change.
     *
     * @param event the event containing the updated resources of one player
     */
    void update(PlayerResourcesUpdatedEvent event);

    /**
     * Called when a player's deck changes.
     *
     * @param event the event containing the updated personal deck of one player
     */
    void update(PlayerDeckUpdatedEvent event);

    /**
     * Called when the game ends and the final ranking becomes available.
     *
     * @param event the event containing the final ranking
     */
    void update(GameEndedEvent event);
}
