package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamelogic.GameBuilder;
import it.polimi.ingsw.server.model.observer.event.PlaceTotemTurnChangedEvent;
import it.polimi.ingsw.server.model.observer.event.PlayerResourcesUpdatedEvent;

public class SetupState implements IGameState {

    @Override
    public void nextState(Game game) {
        game.setGameState(new PlacingTotemState());
    }

    /**
     * Executes the setup phase of the game.
     * And notifies the observers about:
     * each player's initial resources
     * the first player who must place a totem
     *
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        GameBuilder builder = new GameBuilder(game);

        // Prepare the board, decks, turn order and initial resources.
        builder.prepareGame();

        // The setup is complete, so the next active phase is placing totems.
        nextState(game);

        notifyInitialResources(game);
        notifyCurrentPlacingPlayer(game);
    }

    /**
     * Notifies observers about the initial resources of all players.
     *
     * @param game the current game context
     */
    private void notifyInitialResources(Game game) {
        for (Player player : game.getPlayers().getPlayers()) {
            game.getObserverManager().notifyObservers(
                    new PlayerResourcesUpdatedEvent(
                            game.getGameId(),
                            player.getNickname(),
                            player.getFood(),
                            player.getPP()
                    )
            );
        }
    }

    /**
     * Notifies observers about the player who must place the first totem.
     *
     * @param game the current game context
     */
    private void notifyCurrentPlacingPlayer(Game game) {
        Player currentPlayer = game.getBoard().getTurnOrderBox().getSlots().stream()
                .filter(slot -> !slot.isFree())
                .map(TurnOrderSlot::getPlacedPlayer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No player is available to place a totem."));

        game.getObserverManager().notifyObservers(
                new PlaceTotemTurnChangedEvent(game.getGameId(), currentPlayer.getNickname())
        );
    }
}
