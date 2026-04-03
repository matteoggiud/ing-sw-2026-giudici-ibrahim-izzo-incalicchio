package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.core.Game;

import java.util.Stack;

public class EventResolver {
    private final Game game;

    public EventResolver(Game game) {
        this.game = game;
    }

    public int ResolveEvent(Stack<EventCard> eventCards) {
        for (EventCard eventCard : eventCards) {

        }
        return 0;
    }
}
