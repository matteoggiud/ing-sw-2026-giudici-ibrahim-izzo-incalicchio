package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.board.TurnOrderSlot;
import it.polimi.ingsw.model.core.*;

public class PlacingTotemState implements IGameState{
    @Override
    public void nextState(Game game) {
        game.setGameState(new ResolveActionsState());
    }

    @Override
    public void placeTotem(Game game, Player player, OfferBox offerBox) {
        if (game.getCurrentPlayer() != player) {
            throw new IllegalStateException("It's not your turn to place a totem.");
        }
        if (!offerBox.isEmpty()) {
            throw new IllegalStateException("You cannot place a totem on an occupied offer box.");
        }
        offerBox.placeTotem(player);
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
