package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.board.TurnOrderSlot;
import it.polimi.ingsw.model.core.*;

public class PlacingTotemState implements IGameState {
    @Override
    public void nextState(Game game) {
        ResolveActionsState resolveActionsState = new ResolveActionsState();
        game.setGameState(resolveActionsState);
        resolveActionsState.executePhase(game);
    }


    /**
     * This method allows a player to place their totem on the offer box during the Placing Totem phase.
     *
     * @param game     the current game context
     * @param player   the player making the action
     * @param offerBox the offer box where the player wants to place their totem
     * @throws IllegalStateException if it's not the player's turn to place a totem or if the offer box is already occupied
     */
    @Override
    public void placeTotem(Game game, Player player, OfferBox offerBox) throws IllegalStateException {
        // Check if it's the player's turn to place a totem
        if (!getCurrentPlayer(game.getBoard()).equals(player)) {
            throw new IllegalStateException("It's not your turn to place a totem.");
        }

        // Check if the offer box is free before placing the totem
        if (!offerBox.isFree()) {
            throw new IllegalStateException("You cannot place a totem on an occupied offer box.");
        }

        // Place the player's totem on the offer box and remove the player from the turn order box
        try {
            offerBox.placePlayer(player);
            game.getBoard().getTurnOrderBox().removePlayer(player);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("You cannot place a totem on this offer box.");
        }

        // Check if all players have placed their totems, if so, transition to the next state
        if (game.getBoard().getTurnOrderBox().isCompletelyEmpty()) {
            nextState(game);
        }
    }

    /**
     * This method retrieves the current player from the board's turn order box.
     *
     * @param board the game board
     * @return the current player who is placing their totem
     * @throws IllegalStateException if everyone has just placed the totem in the offer box
     */
    private Player getCurrentPlayer(Board board) throws IllegalStateException {
        return board.getTurnOrderBox().getSlots().stream()
                .filter(slot -> !slot.isFree())
                .map(TurnOrderSlot::getPlacedPlayer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Placed all totems, no other totem have to be placed"));
    }

}
