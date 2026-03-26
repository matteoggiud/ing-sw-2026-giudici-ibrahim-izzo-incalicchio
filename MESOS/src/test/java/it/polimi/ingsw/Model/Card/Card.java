package it.polimi.ingsw.Model.Card;

import java.util.UUID;

public abstract class Card {
    private final UUID id;
    private final String name;
    private final Era era;
    private final int minNumPlayers;


    public Card(UUID id, String name, Era era, int minNumPlayers) {
        this.id = id;
        this.name = name;
        this.era = era;
        this.minNumPlayers = minNumPlayers;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Era getEra() {
        return era;
    }

    public int getMinNumPlayers() {
        return minNumPlayers;
    }

    public boolean isAvailableForPlayerCount(int countPlayers){
        return countPlayers >= minNumPlayers;
    }
}

