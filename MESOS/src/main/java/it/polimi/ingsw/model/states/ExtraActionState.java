package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.building.extraction.IExtraActionEffect;
import it.polimi.ingsw.model.gamelogic.ActionResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the phase where players with a specific building effect
 * must resolve one extra card draw from the upper row.
 */
public class ExtraActionState implements IGameState {

    private final Set<Player> pendingPlayers = new HashSet<>();
    private boolean initialized = false;

    /**
     * Advances the game to the end round phase and immediately executes it.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        EndRoundState endRoundState = new EndRoundState();
        game.setGameState(endRoundState);
        endRoundState.executePhase(game);
    }

    /**
     * Initializes the pending extra actions.
     * If no player has an extra action, the game immediately advances to the end round phase.
     *
     * @param game the current game context
     * @throws IllegalArgumentException if the game is null
     */
    @Override
    public void executePhase(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }

        if (!initialized) {
            initializePendingPlayers(game);
            initialized = true;
        }

        if (pendingPlayers.isEmpty()) {
            nextState(game);
        }
    }

    /**
     * Checks whether the given player still has a pending extra action to resolve.
     *
     * @param player the player to check
     * @return true if the player has a pending extra action, false otherwise
     */
    @Override
    public boolean getExtraAction(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        return pendingPlayers.contains(player);
    }

    /**
     * Resolves the player's mandatory extra action.
     * Extra actions can only draw one card from the upper row.
     *
     * @param game the current game context
     * @param player the player resolving the extra action
     * @param selectedCard the upper-row card selected by the player
     * @param fromUpperRow true if the card is from the upper row
     * @throws IllegalArgumentException if any required argument is null
     * @throws IllegalStateException if the player has no pending extra action
     */
    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        if (game == null || player == null || selectedCard == null) {
            throw new IllegalArgumentException("Game, player and selectedCard cannot be null.");
        }

        if (!getExtraAction(player)) {
            throw new IllegalStateException("You don't have any extra action to resolve.");
        }

        ActionResolver resolver = new ActionResolver();
        resolver.resolveExtraAction(game, player, selectedCard, fromUpperRow);

        pendingPlayers.remove(player);
        executePhase(game);
    }

    /**
     * Finds all players that must resolve an extra action during this phase.
     *
     * @param game the current game context
     */
    private void initializePendingPlayers(Game game) {
        for (Player player : game.getPlayers().getPlayers()) {
            if (hasExtraActionEffect(player)) {
                pendingPlayers.add(player);
            }
        }
    }

    /**
     * Checks whether the player owns at least one building effect that grants an extra action.
     *
     * @param player the player to check
     * @return true if the player owns an extra action effect, false otherwise
     */
    private boolean hasExtraActionEffect(Player player) {
        return player.getPlayerDeck().getActiveExtractionEffects().stream()
                .anyMatch(IExtraActionEffect::hasExtraAction);
    }
}
