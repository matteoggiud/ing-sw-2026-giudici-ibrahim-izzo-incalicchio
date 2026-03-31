package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.TribeCard;

import java.util.LinkedList;

/**
 * This class represents the tribe deck, which is a collection of tribe cards that players can draw from during the game.
 */
public class TribeDeck {

    /**
     * The tribe deck is a linked list of tribe cards.
     * We used the linked list so that the draw has a
     * complexity of O(1), meanwhile the ArrayList O(n)
     */
    private LinkedList<TribeCard> TribeDeck;

    /**
     * Constructor for the TribeDeck class, it initializes the tribe deck with an empty list of tribe cards.
     */
    public TribeDeck() {
        this.TribeDeck = new LinkedList<>();
    }

    /**
     * This method allows a player to draw a tribe card from the tribe deck.
     * @return The Card drawn by the player
     * @throws IllegalStateException if the tribe deck is empty and there are no cards to draw.
     */
    public TribeCard draw() throws IllegalStateException {
        if(this.TribeDeck.isEmpty()){ throw new IllegalStateException("TribeDeck is empty"); }
        return this.TribeDeck.removeFirst();
    }

    public boolean isEmpty(){
        return this.TribeDeck.isEmpty();
    }
}
