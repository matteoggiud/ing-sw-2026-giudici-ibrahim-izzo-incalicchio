package it.polimi.ingsw.model.effect.building.modifiers;

import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;

/**
 * You get Doubled PP from the Shamanic Event if you win the Shamanic Ritual.
 */
public class ShamanicDoubleBuildingEffect implements IBuildingEffect, IEventModifier {

    // IEventModifier methods

    /**
     * Checks if the modifier is applicable to the given event type.
     *
     * @param eventType the type of event to check for applicability.
     * @return true if the modifier is applicable to the given event type, false otherwise.
     */
    @Override
    public boolean isSupported(EventType eventType) {
        return eventType == EventType.SHAMANIC_RITUAL;
    }

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
