package it.polimi.ingsw.model.board;
import java.util.List;
import java.util.ArrayList;


import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.TribeCard;

/**
 * This class represents the tribe row of the board.
 */
public class TribeRow {
    /**
     * List of tribe cards in the tribe row.
     */
    private final List<TribeCard> tribeCards = new ArrayList<>();


    /**
     * Adds a tribe card to the tribe row.
     *
     * @param card the tribe card to be added to the tribe row
     */
    public void add(TribeCard card) {
        this.tribeCards.add(card);
    }

    /**
     * Removes a tribe card from the tribe row.
     *
     * @param card the tribe card to be removed from the tribe row
     */
    public void remove(TribeCard card) {
        this.tribeCards.remove(card);
    }

    /**
     * Clears all tribe cards from the tribe row.
     */
    public void clear() {
        this.tribeCards.clear();
    }

    /**
     * Gets the list of character cards in the tribe row.
     *
     * @return the list of character cards in the tribe row
     */
    public List<CharacterCard> getCharacterCards() {
        ArrayList<CharacterCard> characters = new ArrayList<>();

        for (TribeCard tribeCard : this.tribeCards)
            if (tribeCard instanceof CharacterCard characterCard) {
                characters.add(characterCard);
            }

        return characters;
    }

    /**
     * Gets the list of event cards in the tribe row.
     *
     * @return the list of event cards in the tribe row
     */
    public List<EventCard> getEventCards() {
        ArrayList<EventCard> events = new ArrayList<>();

        for (TribeCard tribeCard : this.tribeCards) {
            if (tribeCard instanceof EventCard eventCard) {
                events.add(eventCard);
            }
        }
        return events;
    }

    /**
     * Transfers the tribe cards from another tribe row to this tribe row.
     *
     * @param other the other tribe row from which the tribe cards will be transferred.
     */
    public void transferTribeCardsFrom(TribeRow other) {
        this.tribeCards.addAll(other.tribeCards);
        this.clear();
    }

    /**
     * Copies the tribe cards from this tribe row to another tribe row.
     * @param other the other tribe row to which the tribe cards will be copied
     */
    public void copyTribeCardsTo(TribeRow other) {
        other.tribeCards.addAll(this.tribeCards);
    }
}
