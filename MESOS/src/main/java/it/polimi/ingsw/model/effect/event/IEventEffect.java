package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.effect.EffectEventResult;

import java.util.List;

public interface IEventEffect {
    /**
     * Identifies the type of event that triggers this effect.
     *
     * @return the EventType that triggers this effect.
     */
    EventType getEventEffectType();

    /**
     * Compute the base effect of the event card, without applying any modifiers.
     *
     * @param game the current game state.
     * @param eventCard the event card for which the effect is being calculated.
     * @return a list of EffectEventResult representing the base effect of the event card.
     */
    List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard);
}
