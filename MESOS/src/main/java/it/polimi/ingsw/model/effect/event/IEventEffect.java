package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.effect.EffectEventResult;

public interface IEventEffect {
    EventType getEventEffectType();
    EffectEventResult ApplyEffect(Game game, EventCard eventCard);
}
