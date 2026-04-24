package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamelogic.ActionResolver;
import it.polimi.ingsw.server.model.observer.dto.BoardDTO;
import it.polimi.ingsw.server.model.observer.event.BoardUpdatedEvent;
import it.polimi.ingsw.server.model.observer.event.PlayerResourcesUpdatedEvent;
import it.polimi.ingsw.server.model.observer.event.ResolveActionTurnChangedEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state of the game where players resolve their actions
 * after placing their totems on the offer track.
 */
public class ResolveActionsState implements IGameState {

    private final ActionResolver actionResolver = new ActionResolver();

    /**
     * Executes the action resolution phase.
     * Automatic skips are processed first. If there are still players on the offer track,
     * observers are notified about the current board state and whose turn it is next.
     *
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        Map<String, ResourceSnapshot> resourcesBefore = capturePlayerResources(game);

        actionResolver.resolveAutomaticSkips(game);
        notifyChangedPlayerResources(game, resourcesBefore);

        if (game.getBoard().getOfferTrack().getBoxes().stream().allMatch(box -> box.isFree())) {
            nextState(game);
            return;
        }

        notifyBoardUpdate(game);
        notifyCurrentResolvingPlayer(game);
    }

    /**
     * Advances the game to the extra action phase.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        ExtraActionState extraActionState = new ExtraActionState();
        game.setGameState(extraActionState);
        extraActionState.executePhase(game);
    }

    /**
     * Resolves the current player's action and updates the board, resources and turn if needed.
     *
     * @param game the current game context
     * @param player the player resolving the action
     * @param selectedCard the selected card
     * @param fromUpperRow true if the card comes from the upper row, false otherwise
     */
    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        Map<String, ResourceSnapshot> resourcesBefore = capturePlayerResources(game);

        actionResolver.resolveRegularAction(game, player, selectedCard, fromUpperRow);
        notifyChangedPlayerResources(game, resourcesBefore);

        executePhase(game);
    }

    /**
     * Notifies observers about the updated board state.
     *
     * @param game the current game context
     */
    private void notifyBoardUpdate(Game game) {
        game.getObserverManager().notifyObservers(
                new BoardUpdatedEvent(game.getGameId(), BoardDTO.fromGame(game))
        );
    }

    /**
     * Notifies observers about the player who must resolve the next action.
     *
     * @param game the current game context
     */
    private void notifyCurrentResolvingPlayer(Game game) {
        Player currentPlayer = game.getBoard().getOfferTrack().getCurrentPlayer();
        game.getObserverManager().notifyObservers(
                new ResolveActionTurnChangedEvent(game.getGameId(), currentPlayer.getNickname())
        );
    }

    /**
     * Captures the current resources of all players in the game.
     *
     * @param game the current game context
     * @return a map from player nickname to resource snapshot
     */
    private Map<String, ResourceSnapshot> capturePlayerResources(Game game) {
        Map<String, ResourceSnapshot> snapshots = new HashMap<>();

        for (Player currentPlayer : game.getPlayers().getPlayers()) {
            snapshots.put(
                    currentPlayer.getNickname(),
                    new ResourceSnapshot(currentPlayer.getFood(), currentPlayer.getPP())
            );
        }

        return snapshots;
    }

    /**
     * Notifies observers only for players whose resources changed.
     *
     * @param game the current game context
     * @param resourcesBefore the resources captured before the action
     */
    private void notifyChangedPlayerResources(Game game, Map<String, ResourceSnapshot> resourcesBefore) {
        for (Player currentPlayer : game.getPlayers().getPlayers()) {
            ResourceSnapshot before = resourcesBefore.get(currentPlayer.getNickname());

            if (before == null) {
                continue;
            }

            if (before.food() != currentPlayer.getFood() || before.prestigePoints() != currentPlayer.getPP()) {
                notifyPlayerResourcesUpdate(game, currentPlayer);
            }
        }
    }

    /**
     * Notifies observers about the updated resources of one player.
     *
     * @param game the current game context
     * @param player the player whose resources changed
     */
    private void notifyPlayerResourcesUpdate(Game game, Player player) {
        game.getObserverManager().notifyObservers(
                new PlayerResourcesUpdatedEvent(
                        game.getGameId(),
                        player.getNickname(),
                        player.getFood(),
                        player.getPP()
                )
        );
    }

    /**
     * Immutable snapshot of one player's resources at a specific moment.
     *
     * @param food the food amount
     * @param prestigePoints the prestige points amount
     */
    private record ResourceSnapshot(int food, int prestigePoints) {
    }
}
