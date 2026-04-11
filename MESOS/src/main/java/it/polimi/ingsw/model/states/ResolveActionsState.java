package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.*;

import java.util.ArrayList;

public class ResolveActionsState implements IGameState {
    @Override
    public void nextState(Game game) {
        game.setGameState(new EndRoundState());
    }

    @Override
    public void resolveAction(Game game, Player player, ArrayList<Card> upperCards, ArrayList<Card> lowerCards) {
        Board board = game.getBoard();

        // TODO: handle the case where the box gives only food
        // Check if the player is the current player
        if (!getCurrentPlayer(board).equals(player)) {
            throw new IllegalStateException("It's not your turn to resolve an action.");
        }
        // Check if the player has picked the correct number of cards according to the offer box
        OfferBox box = board.getOfferTrack().getBoxByPlayer(player);
        if (upperCards.size() != box.getUpperRowPicks() || lowerCards.size() != box.getLowerRowPicks()) {
            throw new IllegalStateException("You must pick the exact number of cards indicated by the offer box.");
        }

        // Add the picked cards to the player's deck
        player.addCardsInDeck(upperCards);
        player.addCardsInDeck(lowerCards);

        //TODO: understand where to put the payment of the building cards
    }

    /**
     * This method retrieves the current player from the board's turn order box.
     *
     * @param board the game board
     * @return the current player who is placing their totem
     * @throws IllegalStateException if everyone has just placed the totem in the offer box
     */
    private Player getCurrentPlayer(Board board) throws IllegalStateException {
        return board.getOfferTrack().getBoxes().stream()
                .filter(box -> !box.isFree())
                .map(OfferBox::getPlacedPlayer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Everyone has just taken the cards"));
    }
}
