package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.building.extraction.IExtraActionEffect;
import it.polimi.ingsw.server.model.gamelogic.ActionResolver;
import it.polimi.ingsw.server.model.observer.dto.mapper.BoardDTOMapper;
import it.polimi.ingsw.server.model.observer.event.BoardUpdatedEvent;
import it.polimi.ingsw.server.model.observer.event.ExtraActionTurnChangedEvent;
import it.polimi.ingsw.server.model.observer.dto.mapper.PlayerInfosDTOMapper;
import it.polimi.ingsw.server.model.observer.event.PlayerInfosChangedEvent;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents the phase where players with a specific building effect
 * must resolve one extra card draw from the upper row.
 */
public class ExtraActionState implements IGameState {

    private final List<Player> pendingPlayers = new ArrayList<>();
    private boolean initialized = false;

    /**
     * Advances the game to the end round phase and immediately executes it.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        game.setGameState(new EndRoundState());
        game.getGameState().executePhase(game);
    }

    /**
     * Initializes and refreshes the pending extra actions.
     * Players that cannot draw any selectable upper-row card are automatically skipped,
     * because the client would have no valid card to select for them.
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

        resolveAutomaticExtraActionSkips(game);

        if (pendingPlayers.isEmpty()) {
            nextState(game);
            return;
        }

        notifyBoardUpdate(game);
        notifyCurrentExtraActionPlayer(game);
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

        notifyPlayerInfosUpdate(game);

        pendingPlayers.remove(player);
        executePhase(game);
    }


    /**
     * Automatically removes pending players that cannot resolve their extra action.
     * This handles the edge case where the upper row has no selectable card for them.
     *
     * @param game the current game context
     */
    private void resolveAutomaticExtraActionSkips(Game game) {
        ActionResolver resolver = new ActionResolver();
        pendingPlayers.removeIf(player -> !resolver.canResolveExtraAction(game, player));
    }

    /**
     * Finds all players that own an extra action effect.
     * Players are added first, then automatic skip logic removes those
     * who cannot actually resolve the extra draw.
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

    /**
     * Notifies observers about the updated board state.
     *
     * @param game the current game context
     */
    private void notifyBoardUpdate(Game game) {
        game.getObserverManager().notifyObservers(
                new BoardUpdatedEvent(game.getGameId(), BoardDTOMapper.toBoardDTO(game))
        );
    }

    /**
     * Notifies observers about the player who must resolve the next extra action.
     *
     * @param game the current game context
     */
    private void notifyCurrentExtraActionPlayer(Game game) {
        Player currentPlayer = pendingPlayers.get(0);
        game.getObserverManager().notifyObservers(
                new ExtraActionTurnChangedEvent(game.getGameId(), currentPlayer.getNickname())
        );
    }



    private void notifyPlayerInfosUpdate(Game game) {
        game.getObserverManager().notifyObservers(
                new PlayerInfosChangedEvent(
                        game.getGameId(),
                        PlayerInfosDTOMapper.toPlayerInfoDTOList(game.getPlayers().getPlayers())
                )
        );
    }

}
