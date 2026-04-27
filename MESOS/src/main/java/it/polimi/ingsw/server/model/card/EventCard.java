package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.effect.event.IEventEffect;
import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.core.Player;


import java.util.List;

/**
 * This class represents an event card, which is a type of card in the game.
 * Event cards can have special effects (IEventEffect) and exists in different types (EventType).
 */
public class EventCard extends TribeCard {
    /**
     * Indicates whether this event card is a special card (only 2 per game), in that case they must be placed at the end of the deck.
     */
    private final boolean isFinalEvent;

    /**
     * The effect associated with this event card, which defines what happens when you have to resolve the event.
     */
    private final IEventEffect eventEffect;

    /**
     * The amount of Food gained when the event is resolved successfully.
     */
    private final int gainedFood;

    /**
     * The amount of PP gained when the event is resolved successfully.
     */
    private final int gainedPP;

    /**
     * The amount of resources lost Food (malus) when the event is resolved unsuccessfully.
     */
    private final int malusFood;

    /**
     * The amount of resources lost PP (malus) when the event is resolved unsuccessfully.
     */
    private final int malusPP;

    /**
     * The threshold value that determines who receives bonus or malus.
     */
    private final int threshold;


    /**
     * Construct a new EventCard with specific properties.
     *
     * @param cardId        the unique identifier of the card
     * @param name          the name of the event card
     * @param era           the era to which the event card belongs
     * @param minNumPlayers the minimum number of players required to use this event card
     * @param isFinalEvent  indicates whether this event card is a special card
     * @param eventEffect   the effect associated with this event card
     * @param gainedFood    the amount of food gained
     * @param gainedPP      the amount of PP gained
     * @param malusFood     the amount of food lost (malus)
     * @param malusPP       the amount of food lost (malus)
     * @param threshold     the threshold value that determines who receives bonus or malus
     * @param imageKey      the key used to retrieve the card's image from the resource manager
     * @throws IllegalArgumentException if numeric constraints are violated or something is null
     */
    public EventCard(int cardId, String name, Era era, int minNumPlayers, boolean isFinalEvent, IEventEffect eventEffect, int gainedFood, int malusFood, int gainedPP, int malusPP, int threshold, String imageKey) throws IllegalArgumentException {
        super(cardId, name, era, minNumPlayers, imageKey);

        if (eventEffect == null)
            throw new IllegalArgumentException("eventEffect cannot be null");

        if (gainedFood < 0)
            throw new IllegalArgumentException("gained cannot be negative");

        if (malusFood < 0)
            throw new IllegalArgumentException("malus cannot be negative");

        if (gainedPP < 0)
            throw new IllegalArgumentException("gained cannot be negative");

        if (malusPP < 0)
            throw new IllegalArgumentException("malus cannot be negative");

        if (threshold < 0)
            throw new IllegalArgumentException("threshold cannot be negative");

        this.isFinalEvent = isFinalEvent;
        this.eventEffect = eventEffect;
        this.gainedFood = gainedFood;
        this.malusFood = malusFood;
        this.malusPP = malusPP;
        this.gainedPP = gainedPP;
        this.threshold = threshold;
    }

    /**
     * Indicates whether this event card is a special card.
     *
     * @return true if this event card is a special card, false otherwise.
     */
    @Override
    public boolean isFinalEvent() {
        return isFinalEvent;
    }

    /**
     * Indicates whether this event card should be placed in the upper row during the initial setup
     *
     * @return true if this event card should be placed in the upper row, false otherwise.
     */
    @Override
    public boolean goesInUpperRow() {
        return true;
    }

    /**
     * Gets the effect associated with this event card.
     *
     * @return the event effect of this event card.
     */
    public IEventEffect getEventEffect() {
        return eventEffect;
    }

    /**
     * Gets the amount of Food gained.
     *
     * @return the amount of resources gained.
     */
    public int getGainedFood() {
        return gainedFood;
    }

    /**
     * Gets the amount of PP gained.
     *
     * @return the amount of resources gained.
     */
    public int getGainedPP() {
        return gainedPP;
    }

    /**
     * Gets the amount of Food lost (malus).
     *
     * @return the amount of resources lost.
     */
    public int getMalusFood() {
        return malusFood;
    }

    /**
     * Gets the amount of Food lost (malus).
     *
     * @return the amount of resources lost.
     */
    public int getMalusPP() {
        return malusPP;
    }

    /**
     * Gets the threshold value that determines who receives bonus or malus.
     *
     * @return the threshold value.
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Event cards cannot be added to a player's deck
     *
     * @param playerDeck the player's deck to which this card would be added
     * @throws UnsupportedOperationException always, since event cards cannot be added to a player's deck
     */
    @Override
    public void addTo(PlayerDeck playerDeck) {
        throw new UnsupportedOperationException("Event cards cannot be added to a player's deck");
    }

    @Override
    public void addToEventList(List<EventCard> eventList) {
        eventList.add(this);
    }

    @Override
    public void validateSelectableFrom(CardRow row) {
        throw new IllegalArgumentException("Event cards cannot be selected from the offer rows.");
    }

    @Override
    public void resolveFromOfferRow(Player player, CardRow row) {
        throw new IllegalArgumentException("Event cards cannot be selected from the offer rows.");
    }

}
