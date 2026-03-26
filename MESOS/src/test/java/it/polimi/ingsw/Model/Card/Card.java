package it.polimi.ingsw.Model.Card;

import java.util.UUID;

public abstract class Card {
    UUID id;
    String name;
    Era era;
    int numMinPlayers;
}
