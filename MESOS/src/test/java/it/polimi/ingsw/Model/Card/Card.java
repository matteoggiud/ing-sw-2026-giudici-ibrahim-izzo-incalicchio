package it.polimi.ingsw.model.Card;

import java.util.UUID;

public abstract class Card {
    UUID id;
    String name;
    Era era;
    int numMinPlayers;
}
