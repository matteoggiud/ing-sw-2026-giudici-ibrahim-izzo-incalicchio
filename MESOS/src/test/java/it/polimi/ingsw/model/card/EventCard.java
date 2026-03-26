package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.effect.event.IEventEffect;


public class EventCard extends Card {
    private final boolean isSpecialCard;
    private final IEventEffect eventEffect;
    private final int gained;
    private final int malus;
    private final int threshold;


    public EventCard(String name, Era era, int minNumPlayers, boolean isSpecialCard, IEventEffect eventEffect, int gained, int malus, int threshold) {
        super(name, era, minNumPlayers);
        this.isSpecialCard = isSpecialCard;
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
}
