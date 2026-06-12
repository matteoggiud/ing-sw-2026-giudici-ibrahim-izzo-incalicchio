package it.polimi.ingsw.server.model.core;

import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.board.OfferTrack;
import it.polimi.ingsw.server.model.board.TurnOrderBox;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.deck.BuildingDeck;

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
     * Constructs a new empty Board.
     */
    public Board() {
        this.offerTrack = new OfferTrack();
        this.upperRow = new CardRow();
        this.lowerRow = new CardRow();
        this.turnOrderBox = new TurnOrderBox();
    }

    /**
     * Gets the offer track of the board.
     *
     * @return the offer track of the board
     */
    public OfferTrack getOfferTrack() {
        return offerTrack;
    }

    /**
     * Gets the upper row of cards on the board.
     *
     * @return the upper row of cards on the board
     */
     public CardRow getUpperRow() {
        return upperRow;
    }

    /**
     * Gets the lower row of cards on the board.
     *
     * @return the lower row of cards on the board
     */
    public CardRow getLowerRow() {
        return lowerRow;
    }

    /**
     * Gets the turn order of the board.
     *
     * @return the turn order of the board
     */
    public TurnOrderBox getTurnOrderBox() {
        return turnOrderBox;
    }

    /**
     * Adds a tribe card to the upper row of the board.
     *
     * @param tribeCard the tribe card to be added to the upper row
     * @throws IllegalArgumentException if the card is null
     */
    public void addTribeUpperRow(TribeCard tribeCard) throws IllegalArgumentException {
        upperRow.addCard(tribeCard);
    }

    /**
     * Clears all buildings from the lower row of the board, making it empty.
     */
    public void clearLowerBuildingRow() {
        lowerRow.clearBuildings();
    }

    /**
     * Transfers all building cards from the upper row to the lower row, effectively moving them down the board.
     */
    public void transferUpperBuildingToLower() {
        lowerRow.transferBuildingRowFrom(upperRow);
    }

    /**
     * Adds building cards from the given building deck to the upper row of the board.
     *
     * @param buildingDeck the building deck from which to copy building cards to the upper row
     */
    public void placeInUpperBuildingRow(BuildingDeck buildingDeck) {
        upperRow.addBuildingRowFrom(buildingDeck);
    }

    /**
     * Transfers all tribe cards from the upper row to the lower row.

     */
    public void transferUpperTribeToLower() {
        lowerRow.transferTribeRowFrom(upperRow);
    }


}
