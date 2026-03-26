package it.polimi.ingsw.model.core;

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
}
