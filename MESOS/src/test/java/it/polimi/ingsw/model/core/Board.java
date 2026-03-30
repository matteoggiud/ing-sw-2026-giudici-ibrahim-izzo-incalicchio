package it.polimi.ingsw.model.core;

import it.polimi.ingsw.model.board.CardRow;
import it.polimi.ingsw.model.board.OfferTrack;
import it.polimi.ingsw.model.board.TurnOrderBox;

/**
 *
 * Represents the game board, which consists of the offer track, two rows of cards (upper and lower), and the turn order box.
 */
public class Board {

    /** The set of cards that every player goes on */
    private final OfferTrack offerTrack;

    /** The upper row of cards on the board. */
    private final CardRow upperRow;

    /** The lower row of cards on the board. */
    private final CardRow lowerRow;

    /** The turn order box, the card which determines the order of player turns. */
    private final TurnOrderBox turnOrderBox;

    /**
     * Constructs a new Board with an offer track, two card rows, and a turn order box.
     */
    public Board() {
        this.offerTrack = new OfferTrack();
        this.upperRow = new CardRow();
        this.lowerRow = new CardRow();
        this.turnOrderBox = new TurnOrderBox();
    }


}
