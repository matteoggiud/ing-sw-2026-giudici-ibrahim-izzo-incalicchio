package it.polimi.ingsw.model.effect.modifiers;

import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectEventResult;

public interface IEventModifier {

    /**
     * Apply the modifier to the given EffectEventResult.
     *
     * @param eventType the type of event that triggered the effect.
     * @param result the EffectEventResult to which the modifier should be applied.
     */
    void applyModifier(EventType eventType, EffectEventResult result);
}
