package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.effect.EffectEventResult;

import java.util.List;

//TODO: understand how to handle modifiers due to gatherer cards and building cards
public interface IEventEffect {
    EventType getEventEffectType();
    List<EffectEventResult> ApplyEffect(Game game, EventCard eventCard);
}
