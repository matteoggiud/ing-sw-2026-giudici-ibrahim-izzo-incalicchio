package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;

import java.util.Map;

/**
 * Interface representing the generic state of the game.
 * Uses the State Design Pattern to handle the game flow.
 */
public interface IGameState {
    /**
     * Advances the game to the next logical state.
     *
     * @param game the current game context
     */
    void nextState(Game game);

    /**
     * Executes automatic logic for the current phase (if any).
     *
     * @param game the current game context
     */
    default void executePhase(Game game) {
    }

    /**
     * A player attempts to place their totem on the board.
     *
     * @param game     the current game context
     * @param player   the player making the action
     * @param offerBox the offer box where the player wants to place their totem
     * @throws IllegalStateException if this action is not allowed in the current state
     */
    default void placeTotem(Game game, Player player, OfferBox offerBox) {
        throw new IllegalStateException("You cannot place a totem during this phase of the game.");
    }


    /**
     * Checks whether the given player has a pending extra action in the current phase.
     * By default, this operation is not allowed outside the extra action phase.
     *
     * @param player the player to check
     * @return true if the player has a pending extra action, false otherwise
     * @throws IllegalStateException if the current phase does not support extra actions
     */
    default boolean getExtraAction(Player player) {
        throw new IllegalStateException("You cannot get extra action during this phase of the game.");
    }


    default void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        throw new IllegalStateException("You cannot take card during this phase of the game.");
    }

    /**
     * Adds a player to the game.
     * By default, players cannot join outside the lobby phase.
     *
     * @param game the current game context
     * @param player the player who wants to join
     * @throws IllegalStateException if players cannot join during the current phase
     */
    default void addPlayer(Game game, Player player) {
        throw new IllegalStateException("You cannot join the game during this phase.");
    }

    /**
     * Removes a player from the game.
     * By default, players cannot leave outside the lobby phase.
     *
     * @param game the current game context
     * @param player the player who wants to leave
     * @throws IllegalStateException if players cannot leave during the current phase
     */
    default void removePlayer(Game game, Player player) {
        throw new IllegalStateException("You cannot leave the game during this phase.");
    }


    /**
     * Returns the final ranking of the players at the end of the game.
     * @param game the current game context
     * @return a map of players and their corresponding final scores, sorted by rank
     */
    default Map<Player, Integer> getFinalRanking(Game game) {
        throw new IllegalStateException("You cannot get final ranking during this phase of the game.");
    }

    /**
     * Checks if the game is in a state where new players can join.
     * @param game the current game context
     * @return true if the game is joinable, false otherwise
     */
    default boolean isJoinable(Game game) {
        return false;
    }


    /**
     *
     * Starts the game by transitioning from the lobby state to the first phase of the game.
     * @param game the current game context
     */
    default void startGame(Game game) {
        throw new IllegalStateException("You cannot start this phase of the game.");
    }

    /**
     * Checks if the game has ended.
     * @return true if the game has ended, false otherwise
     */
    boolean isEnded();


    /**
     * Returns the name of the state for persistence purposes.
     * @return the name of the persistence class for this game state
     */
    String getPersistenceName();
}
