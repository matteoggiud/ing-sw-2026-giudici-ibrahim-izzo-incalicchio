package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.core.Board;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.observer.dto.mapper.BoardDTOMapper;
import it.polimi.ingsw.server.model.observer.event.BoardUpdatedEvent;
import it.polimi.ingsw.server.model.observer.event.PlaceTotemTurnChangedEvent;

public class PlacingTotemState implements IGameState {

    @Override
    public void nextState(Game game) {
        game.setGameState(new ResolveActionsState());
        game.getGameState().executePhase(game);
    }

    @Override
    public void executePhase(Game game) {
        // Notify the first player who must place their totem.
        notifyCurrentPlacingPlayer(game);
    }

    /**
     * Allows a player to place their totem on the offer box during the placing totem phase.
     *
     * @param game     the current game context
     * @param player   the player making the action
     * @param offerBox the offer box where the player wants to place their totem
     * @throws IllegalStateException if it is not the player's turn or the offer box is occupied
     */
    @Override
    public void placeTotem(Game game, Player player, OfferBox offerBox) throws IllegalStateException {
        // Check if it is the player's turn to place a totem.
        if (!getCurrentPlayer(game.getBoard()).equals(player)) {
            throw new IllegalStateException("It's not your turn to place a totem.");
        }

        // Check if the offer box is free before placing the totem.
        if (!offerBox.isFree()) {
            throw new IllegalStateException("You cannot place a totem on an occupied offer box.");
        }

        // Place the player's totem on the offer box and remove the player from the turn order box.
        try {
            offerBox.placePlayer(player);
            game.getBoard().getTurnOrderBox().removePlayer(player);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("You cannot place a totem on this offer box.");
        }

        // Notify observers about the updated board state.
        notifyBoardUpdate(game);

        // If all players have placed their totems, the game moves to action resolution.
        if (game.getBoard().getTurnOrderBox().isCompletelyEmpty()) {
            nextState(game);
            return;
        }

        // Otherwise, notify the next player who must place a totem.
        notifyCurrentPlacingPlayer(game);
    }

    /**
     * Retrieves the current player from the board's turn order box.
     *
     * @param board the game board
     * @return the current player who must place a totem
     * @throws IllegalStateException if all totems have already been placed
     */
    private Player getCurrentPlayer(Board board) throws IllegalStateException {
        return board.getTurnOrderBox().getSlots().stream()
                .filter(slot -> !slot.isFree())
                .map(TurnOrderSlot::getPlacedPlayer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("All totems have already been placed."));
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
     * Notifies observers about the player who must place the next totem.
     *
     * @param game the current game context
     */
    private void notifyCurrentPlacingPlayer(Game game) {
        Player currentPlayer = getCurrentPlayer(game.getBoard());
        game.getObserverManager().notifyObservers(
                new PlaceTotemTurnChangedEvent(game.getGameId(), currentPlayer.getNickname())
        );
    }


}
