package it.polimi.ingsw.model.effect.building.modifiers;

import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectEventResult;

public interface IEventModifier {

    /**
     * Checks if the modifier is applicable to the given event type.
     *
     * @param eventType the type of event to check for applicability.
     * @return true if the modifier is applicable to the given event type, false otherwise.
     */
    boolean isSupported(EventType eventType);

    /**
     * Apply the modifier to the given EffectEventResult.
     *
     * @param eventType the type of event that triggered the effect.
     * @param result the EffectEventResult to which the modifier should be applied.
     */
    void applyModifier(EventType eventType, EffectEventResult result);
}
