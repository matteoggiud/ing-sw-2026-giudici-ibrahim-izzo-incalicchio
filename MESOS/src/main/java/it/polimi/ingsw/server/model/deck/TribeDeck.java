package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.TribeCard;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the tribe deck, which is a collection of tribe cards that players can draw from during the game.
 */
public class TribeDeck {

    /**
     * The tribe deck is a linked list of tribe cards.
     * We used the linked list so that the draw has a
     * complexity of O(1), meanwhile the ArrayList O(n)
     */
    private final LinkedList<TribeCard> tribeDeck;

    /**
     * Constructor for the TribeDeck class, it initializes the tribe deck with an empty list of tribe cards.
     */
    public TribeDeck() {
        this.tribeDeck = new LinkedList<>();
    }

    /**
     * Constructs a new TribeDeck with a given list of tribe cards.
     *
     * @param tribeDeck the list of tribe cards to initialize the deck with (must not be null)
     * @throws IllegalArgumentException if the provided list of tribe cards is null
     */
    public TribeDeck(LinkedList<TribeCard> tribeDeck) throws IllegalArgumentException {
        if (tribeDeck == null || tribeDeck.isEmpty()) {
            throw new IllegalArgumentException("TribeDeck cannot be null.");
        }
        this.tribeDeck = tribeDeck;
    }

    /**
     * This method allows a player to draw a tribe card from the tribe deck.
     * @return The Card drawn by the player
     * @throws IllegalStateException if the tribe deck is empty and there are no cards to draw.
     */
    public TribeCard drawCard() throws IllegalStateException {
        if(this.tribeDeck.isEmpty()){ throw new IllegalStateException("TribeDeck is empty"); }
        return this.tribeDeck.removeFirst();
    }

    /**
     * Sets the tribe deck with a new list of tribe cards.
     *
     * @param tribeDeck the new list of tribe cards to set as the tribe deck (must not be null or empty)
     * @throws IllegalArgumentException if the provided list of tribe cards is null or empty
     */
    public void setTribeDeck(List<TribeCard> tribeDeck) {
        if (tribeDeck == null || tribeDeck.isEmpty()) {
            throw new IllegalArgumentException("TribeDeck cannot be null.");
        }

        this.tribeDeck.clear();
        this.tribeDeck.addAll(tribeDeck);
    }

    public boolean isEmpty(){
        return this.tribeDeck.isEmpty();
    }

    public LinkedList<TribeCard> getTribeDeck() {
        return tribeDeck;
    }
}

