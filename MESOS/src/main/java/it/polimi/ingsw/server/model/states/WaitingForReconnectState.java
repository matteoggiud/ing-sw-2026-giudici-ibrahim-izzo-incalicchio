package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.menu.GameManager;
import it.polimi.ingsw.server.model.observer.event.GameEndedEvent;

import java.util.*;

/**
 * The WaitingForReconnectState class represents a game state where the game is paused and waiting for players to reconnect after a disconnection.
 */
public class WaitingForReconnectState implements IGameState {

    private final ArrayList<String> pendingPlayers;
    private final IGameState originalStateInstance;
    private Timer timeoutTimer;
    private boolean resumed = false;

    // Timeout of 5 minutes
    private static final long TIMEOUT_MS = 1 * 90 * 1000;

    /**
     * Creates a new WaitingForReconnectState with the given original state name and the list of pending players who need to reconnect.
     * @param originalStateName the name of the original state to restore once all players have reconnected, must not be null or blank
     * @param pendingPlayers the list of player nicknames who need to reconnect, must not be null or empty
     */
    public WaitingForReconnectState(String originalStateName, List<String> pendingPlayers) {
        if (originalStateName == null || originalStateName.isBlank()) {
            throw new IllegalArgumentException("Original state name cannot be null or blank.");
        }
        if (pendingPlayers == null || pendingPlayers.isEmpty()) {
            throw new IllegalArgumentException("Pending players cannot be null or empty.");
        }

        this.originalStateInstance = instantiateOriginalState(originalStateName);
        this.pendingPlayers = new ArrayList<>(pendingPlayers);
    }

    /**
     * Starts the timeout timer. If it expires before everyone reconnects, the
     * game ends.
     */
    public void startTimeout(Game game) {
        if (timeoutTimer != null)
            return;

        timeoutTimer = new Timer(true);
        timeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.locked(() -> {
                    // If the timer expires but the game has already resumed, do nothing
                    if (resumed) {
                        return;
                    }

                System.out.println("Time out is over for " + game.getGameId() + ". Closing game.");

                new DataWriting().updateGameInProgress(game.getGameId(), false);

                Map<String, Integer> timeoutRanking = new HashMap<>();
                game.getObserverManager().notifyObservers(new GameEndedEvent(game.getGameId(), timeoutRanking, 0, null));
                GameManager.getInstance().removeGame(game.getGameId());
                });
            }
        }, TIMEOUT_MS);
    }

    /**
     * Executes the waiting phase by starting the timeout timer. If all players reconnect before the timer expires, the game will resume and the original state will be restored.
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        startTimeout(game);
    }

    /**
     * Handles a player trying to join the game. If the player is in the pending list, they are removed from it. If all pending players have reconnected, the timeout timer is cancelled and the game resumes by restoring the original state.
     * @param game the current game context
     * @param player the player who wants to join
     */
    @Override
    public void addPlayer(Game game, Player player) {
        String nickname = player.getNickname();

        if (pendingPlayers.contains(nickname)) {
            // Removes the player from the pending list
            pendingPlayers.remove(nickname);

            System.out.println("Player " + nickname + " rejoined. Waiting for reconnect others " + pendingPlayers.size()
                    + " players.");

            if (pendingPlayers.isEmpty()) {
                // Everyone reconnected before timeout, stop the timer and resume the game
                if (timeoutTimer != null)
                    timeoutTimer.cancel();

                System.out.println("Everyone reconnected to game " + game.getGameId() + ". Resuming game.");

                // Restore the original state and continue!
                nextState(game);
            }

        } else {
            throw new IllegalStateException("The player is not part of the game.");
        }
    }

    /**
     * Transitions the game back to the original state and executes its phase to resume the game.
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        this.resumed = true;
        game.setGameState(originalStateInstance);
        originalStateInstance.executePhase(game);
    }

    /**
     * Handles a player trying to place a totem. Since the game is paused and waiting for players to reconnect, this action is not allowed and an IllegalStateException is thrown.
     * @param game     the current game context
     * @param player   the player making the action
     * @param offerBox the offer box where the player wants to place their totem
     */
    @Override
    public void placeTotem(Game game, Player player, OfferBox offerBox) {
        throw new IllegalStateException("Partita in attesa di riconnessione degli altri giocatori.");
    }

    /**
     * Handles a player trying to resolve an action. Since the game is paused and waiting for players to reconnect, this action is not allowed and an IllegalStateException is thrown.
     * @param game   the current game context
     * @param player the player making the action
     * @param selectedCard the card selected by the player for the action resolution
     * @param fromUpperRow a boolean indicating whether the selected card is from the upper row of the offer box
     */
    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        throw new IllegalStateException("Partita in attesa di riconnessione degli altri giocatori.");
    }

    /**
     * Handles a player trying to resolve an extra action. Since the game is paused and waiting for players to reconnect, this action is not allowed and an IllegalStateException is thrown.
     * @param player the player to check
     * @return always throws an IllegalStateException since the game is paused and waiting for players to reconnect
     */
    @Override
    public boolean getExtraAction(Player player) {
        throw new IllegalStateException("Partita in attesa di riconnessione degli altri giocatori.");
    }

    /**
     * Checks if the game has ended. Since the game is paused and waiting for players to reconnect, it is not considered ended and this method always returns false.
     * @return false since the game is paused and waiting for players to reconnect, it is not considered ended
     */
    @Override
    public boolean isEnded() {
        return false;
    }

    /**
     * Recreates the correct state instance from the string saved in the DTO.
     */
    private IGameState instantiateOriginalState(String stateName) {
        return switch (stateName) {
            case "LobbyState" -> new LobbyState();
            case "SetupState" -> new SetupState();
            case "PlacingTotemState" -> new PlacingTotemState();
            case "ResolveActionsState" -> new ResolveActionsState();
            case "EndRoundState" -> new EndRoundState();
            case "ExtraActionState" -> new ExtraActionState();
            case "ComputeFinalScoreState" -> new ComputeFinalScoreState();
            case "EndedState" -> new EndedState(new java.util.LinkedHashMap<>());
            default -> throw new IllegalArgumentException("Stato sconosciuto: " + stateName);
        };
    }

    /**
     * Returns the name of this state to be saved in the database for persistence.
     * @return the name of this state for persistence purposes
     */
    @Override
    public String getPersistenceName() {
        return "WaitingForReconnectState";
    }
}
