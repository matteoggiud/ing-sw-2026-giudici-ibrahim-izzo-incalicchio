package it.polimi.ingsw.Model.Card;

import java.util.UUID;

public class EventCard extends Card {
    private final boolean isSpecialCard;
    private final EventEffect eventEffect;
    private final int gained;
    private final int malus;
    private final int threshold;


    public EventCard(UUID id, String name, Era era, int minNumPlayers, boolean isSpecialCard, EventEffect eventEffect, int gained, int malus, int threshold) {
        super(id, name, era, minNumPlayers);
        this.isSpecialCard = isSpecialCard;
        this.eventEffect = eventEffect;
        this.gained = gained;
        this.malus = malus;
        this.threshold = threshold;
    }

    public boolean isSpecialCard() {
        return isSpecialCard;
    }

    public EventEffect getEventEffect() {
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
