package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.effect.event.IEventEffect;

/**
 * This class represents an event card, which is a type of card in the game.
 * Event cards can have special effects (IEventEffect) and exists in different types (EventType).
 */
public class EventCard extends Card {
    /**
     * Indicates whether this event card is a special card (only 2 per game), in that case they must be placed at the end of the deck.
     */
    private final boolean isSpecialCard;

    /**
     * The specific type of the event card (i.e., HUNT, SHAMANIC_RITUAL, etc.).
     */
    private final EventType eventType;

    /**
     * The effect associated with this event card, which defines what happens when you have to resolve the event.
     */
    private final IEventEffect eventEffect;

    /**
     * The amount of resources gained when the event is resolved successfully.
     */
    private final int gained;

    /**
     * The amount of resources lost (malus) when the event is resolved unsuccessfully.
     */
    private final int malus;

    /**
     * The threshold value that determines who receives bonus or malus.
     */
    private final int threshold;


    /**
     * Construct a new EventCard with specific properties.
     *
     * @param name          the name of the event card
     * @param era           the era to which the event card belongs
     * @param minNumPlayers the minimum number of players required to use this event card
     * @param isSpecialCard indicates whether this event card is a special card
     * @param eventType     the specific type of the event card
     * @param eventEffect   the effect associated with this event card
     * @param gained        the amount of resources gained
     * @param malus         the amount of resources lost (malus)
     * @param threshold     the threshold value that determines who receives bonus or malus
     * @throws IllegalArgumentException if numeric constraints are violated or something is null
     */
    public EventCard(String name, Era era, int minNumPlayers, boolean isSpecialCard, EventType eventType, IEventEffect eventEffect, int gained, int malus, int threshold) throws IllegalArgumentException {
        super(name, era, minNumPlayers);

        if (eventType == null)
            throw new IllegalArgumentException("eventType cannot be null");

        if (eventEffect == null)
            throw new IllegalArgumentException("eventEffect cannot be null");

        if (gained < 0)
            throw new IllegalArgumentException("gained cannot be negative");

        if (malus < 0)
            throw new IllegalArgumentException("malus cannot be negative");

        if (threshold < 0)
            throw new IllegalArgumentException("threshold cannot be negative");

        this.isSpecialCard = isSpecialCard;
        this.eventType = eventType;
        this.eventEffect = eventEffect;
        this.gained = gained;
        this.malus = malus;
        this.threshold = threshold;
    }

    /**
     * Indicates whether this event card is a special card.
     *
     * @return true if this event card is a special card, false otherwise.
     */
    public boolean isSpecialCard() {
        return isSpecialCard;
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
     * Gets the amount of resources gained.
     *
     * @return the amount of resources gained.
     */
    public int getGained() {
        return gained;
    }

    /**
     * Gets the amount of resources lost (malus).
     *
     * @return the amount of resources lost.
     */
    public int getMalus() {
        return malus;
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
     * Gets the specific type of the event card.
     *
     * @return the event card's type.
     */
    public EventType getEventType() {
        return eventType;
    }
}
