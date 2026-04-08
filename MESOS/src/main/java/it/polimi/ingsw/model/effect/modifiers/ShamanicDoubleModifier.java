package it.polimi.ingsw.model.effect.modifiers;

import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectEventResult;

/**
 * During the Shamanic Event, if you have more shamanic stars than the others, you gain double the PP you would normally gain.
 */
public class ShamanicDoubleModifier implements IEventModifier {
    /**
     * Apply the modifier to the given EffectEventResult.
     *
     * @param eventType the type of event that triggered the effect.
     * @param result    the EffectEventResult to which the modifier should be applied.
     */
    @Override
    public void applyModifier(EventType eventType, EffectEventResult result) {
        if (eventType != EventType.SHAMANIC_RITUAL) {
            throw new IllegalStateException("ShamanicDoubleModifier can only be applied to Shamanic Ritual events");
        }

        // If the player has more shamanic stars than the others, gain double the PP you would normally gain.
        if (result.getPpChange() > 0) {
            result.setPpChange(result.getPpChange() * 2);
        }

    }
}
