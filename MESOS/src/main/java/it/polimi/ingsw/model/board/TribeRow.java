package it.polimi.ingsw.model.board;
import java.util.List;
import java.util.ArrayList;


import it.polimi.ingsw.model.card.TribeCard;
import it.polimi.ingsw.model.card.*;

/**
 * This class represents the tribe row of the board, which is composed by a list of tribe cards.
 * The tribe row can contain both character cards and event cards,
 * which are both subclasses of tribe card. The tribe row has methods to add and remove tribe cards,
 * to get the list of character cards and event cards, and to transfer the tribe cards to another tribe row.
 */
public class TribeRow{
    private ArrayList<TribeCard> tribeCards = new ArrayList<>();


    /**
     * Adds a tribe card to the tribe row.
     * @param card the tribe card to be added to the tribe row
     */
    public void add(TribeCard card) {
        this.tribeCards.add(card);
    }

    /**
     * Removes a tribe card from the tribe row.
     * @param card the tribe card to be removed from the tribe row
     */
    public void remove(TribeCard card) {
        this.tribeCards.remove(card);
    }

    /**
     * Gets the list of character cards in the tribe row.
     * @return the list of character cards in the tribe row
     */
    public List<CharacterCard> getCharacterCards() {
        List<CharacterCard> characters = new ArrayList<>();
               for (int i = 0; i < tribeCards.size(); i++) {
                    TribeCard c = tribeCards.get(i);
                    if (c instanceof CharacterCard) {
                        characters.add((CharacterCard) c);
                    }
        }
        return characters;
    }

    /**
     * Gets the list of event cards in the tribe row.
     * @return the list of event cards in the tribe row
     */
    public List<EventCard> getEventCards() {
        List<EventCard> events = new ArrayList<>();
        for (int i = 0; i < tribeCards.size(); i++) {
            TribeCard c = tribeCards.get(i);
            if (c instanceof EventCard) {
                events.add((EventCard) c);
            }
        }
        return events;
    }

    /**
     *  Transfers the tribe cards from this tribe row to another tribe row.
     * @param other the other tribe row to which the tribe cards will be transferred
     */
    public void transferTribeCardsTo(TribeRow other) {
        for (int i = 0; i < this.tribeCards.size(); i++) {
            other.add(this.tribeCards.get(i));
        }
        this.tribeCards.clear();
    }
}
