package it.polimi.ingsw.model.card;

import java.util.UUID;

public abstract class Card {
    UUID id;
    String name;
    Era era;
    int numMinPlayers;
}
