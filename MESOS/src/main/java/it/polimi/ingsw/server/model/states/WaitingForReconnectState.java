package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.database.DataDepository;
import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.observer.event.GameEndedEvent;

import java.util.*;

public class WaitingForReconnectState implements IGameState {

    private final ArrayList<String> pendingPlayers;
    private final IGameState originalStateInstance;
    private Timer timeoutTimer;

    // Timeout di 5 minuti
    private static final long TIMEOUT_MS = 5 * 60 * 1000;

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
     * Avvia il timer di timeout. Se scade prima che tutti si riconnettano, la
     * partita finisce.
     */
    public void startTimeout(Game game) {
        if (timeoutTimer != null)
            return;

        timeoutTimer = new Timer(true);
        timeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Time out is over for " + game.getGameId() + ". Closing game.");

                new DataWriting().updateGameInProgress(game.getGameId(), false);

                game.getObserverManager().notifyObservers(new GameEndedEvent(game.getGameId(), null, 0, null));
            }
        }, TIMEOUT_MS);
    }

    @Override
    public void executePhase(Game game) {
        startTimeout(game);
    }

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

    @Override
    public void nextState(Game game) {
        game.setGameState(originalStateInstance);
        originalStateInstance.executePhase(game);
    }

    @Override
    public void placeTotem(Game game, Player player, OfferBox offerBox) {
        throw new IllegalStateException("Partita in attesa di riconnessione degli altri giocatori.");
    }

    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        throw new IllegalStateException("Partita in attesa di riconnessione degli altri giocatori.");
    }

    @Override
    public boolean getExtraAction(Player player) {
        throw new IllegalStateException("Partita in attesa di riconnessione degli altri giocatori.");
    }

    @Override
    public boolean isEnded() {
        return false;
    }

    /**
     * Ricrea l'istanza dello stato corretto partendo dalla stringa salvata nel DTO.
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

}
