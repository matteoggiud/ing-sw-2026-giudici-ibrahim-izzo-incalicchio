package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.deck.BuildingDeck;
import it.polimi.ingsw.server.model.core.Player;

/**
 * This class represents the card row of the board, which is composed by a tribe row and a building row.
 */
public class CardRow {

    /**
     * The tribe section of a card row
     */
    private final TribeRow tribeRow;

    /**
     * The building section of a card row
     */
    private final BuildingRow buildingRow;

    /**
     * Constructs a new CardRow with an empty tribe row and an empty building row.
     */
    public CardRow() {
        this.tribeRow = new TribeRow();
        this.buildingRow = new BuildingRow();
    }

    /**
     * Gets the tribe row of this card row.
     */
    public TribeRow getTribeRow() {
        return tribeRow;
    }

    /**
     * Gets the building row of this card row.
     */
    public BuildingRow getBuildingRow() {
        return buildingRow;
    }


    /**
     * Adds a tribe card to the tribe row of this card row.
     *
     * @param tribeCard the tribe card to be added to the tribe row
     * @throws IllegalArgumentException if the tribeCard is null
     */
    public void addCard(TribeCard tribeCard) throws IllegalArgumentException {
        if(tribeCard == null) throw new IllegalArgumentException("Arguments cannot be null");

        tribeRow.add(tribeCard);
    }

    /**
     * Clears all the cards from the tribe row of this card row.
     */
    public void clearBuildings() {
        buildingRow.clear();
    }

    /**
     * Transfers all the building cards from another card row to this card row.
     *
     * @param other the card row from which to transfer the building cards
     */
    public void transferBuildingRowFrom(CardRow other) {
        buildingRow.transferBuildingCardsFrom(other.getBuildingRow());
    }

    /**
     * Adds all the building cards from a building deck to the building row of this card row.
     * @param buildingDeck the building deck from which to add the building cards to the building row
     */
    public void addBuildingRowFrom(BuildingDeck buildingDeck) {
        buildingRow.add(buildingDeck);
    }

    /**
     * Transfers all the tribe cards from another card row to this card row.
     *
     * @param other the card row from which to transfer the tribe cards
     */
    public void transferTribeRowFrom(CardRow other) {
        this.tribeRow.transferTribeCardsFrom(other.getTribeRow());
    }

    /**
     * Clears all the cards from this card row, including both the tribe row and the building row.
     */
    public void clearRow() {
        tribeRow.clear();
        buildingRow.clear();
    }

    /**
     * Counts the total number of cards in this card row, including both the tribe row and the building row.
     * @return the total number of cards in this card row
     */
    public int countRow() {
        return tribeRow.count() + buildingRow.count();
    }

    /**
     * Checks if there is at least one card in this card row that can be selected by the specified player.
     * @param player the player for whom to check if there is at least one selectable card in this card row
     * @return true if there is at least one card in this card row that can be selected by the specified player, false otherwise
     */
    public boolean hasSelectableCardFor(Player player) {
        for (CharacterCard characterCard : tribeRow.getCharacterCards()) {
            if (characterCard.canBeSelectedFrom(this, player)) {
                return true;
            }
        }

        for (BuildingCard buildingCard : buildingRow.getBuildingCards()) {
            if (buildingCard.canBeSelectedFrom(this, player)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets a selectable card by its id from this card row.
     * @param cardId the id of the card to be retrieved
     * @return the card with the specified id if it is selectable from this card row, otherwise throws an exception
     * @throws IllegalArgumentException if the card id is not positive, if the card with
     */
    public Card getSelectableCardById(int cardId) {
        if (cardId <= 0) {
            throw new IllegalArgumentException("Card id must be positive.");
        }

        for (CharacterCard characterCard : tribeRow.getCharacterCards()) {
            if (characterCard.getCardId() == cardId) {
                return characterCard;
            }
        }

        for (BuildingCard buildingCard : buildingRow.getBuildingCards()) {
            if (buildingCard.getCardId() == cardId) {
                return buildingCard;
            }
        }

        for (var eventCard : tribeRow.getEventCards()) {
            if (eventCard.getCardId() == cardId) {
                throw new IllegalArgumentException("Event cards cannot be selected from the offer rows.");
            }
        }

        throw new IllegalArgumentException("Selected card is not available in this row: " + cardId);
    }


}
