package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.effect.event.IEventEffect;

/**
 * This class represents an event card, which is a type of card in the game.
 * Event cards can have special effects (IEventEffect) and exists in different types (EventType).
 */
public class EventCard extends Card {
    /** Indicates whether this event card is a special card (only 2 per game), in that case they must be placed at the end of the deck. */
    private final boolean isSpecialCard;

    /** The specific type of the event card (i.e., HUNT, SHAMANIC_RITUAL, etc.). */
    private final EventType eventType;

    /** The effect associated with this event card, which defines what happens when you have to resolve the event. */
    private final IEventEffect eventEffect;

    /** The amount of resources gained when the event is resolved successfully. */
    private final int gained;

    /** The amount of resources lost (malus) when the event is resolved unsuccessfully. */
    private final int malus;

    /** The threshold value that determines who receives bonus or malus. */
    private final int threshold;


    /**
     * Construct a new EventCard with specific properties.
     *
     * @param name the name of the event card
     * @param era the era to which the event card belongs
     * @param minNumPlayers the minimum number of players required to use this event card
     * @param isSpecialCard indicates whether this event card is a special card
     * @param eventType the specific type of the event card
     * @param eventEffect the effect associated with this event card
     * @param gained the amount of resources gained
     * @param malus the amount of resources lost (malus)
     * @param threshold the threshold value that determines who receives bonus or malus
     */
    public EventCard(String name, Era era, int minNumPlayers, boolean isSpecialCard, EventType eventType, IEventEffect eventEffect, int gained, int malus, int threshold) {
        super(name, era, minNumPlayers);
        this.isSpecialCard = isSpecialCard;
        this.eventType = eventType;
        this.eventEffect = eventEffect;
        this.gained = gained;
        this.malus = malus;
        this.threshold = threshold;
    }

    public boolean isSpecialCard() {
        return isSpecialCard;
    }

    public IEventEffect getEventEffect() {
        return eventEffect;
    }

    public int getGained() {
        return gained;
    }

    public int getMalus() {
        return malus;
    }

    public int getThreshold() {
        return threshold;
    }

    public EventType getEventType() {
        return eventType;
    }
}
