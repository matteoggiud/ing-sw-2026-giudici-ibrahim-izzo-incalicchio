package it.polimi.ingsw.model.effect.modifiers;

import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectEventResult;

/**
 * During the shamanic ritual, if you have less shamanic stars than other players you don't lose prestige points
 */
public class ShamanicImmunityModifier implements IEventModifier {

    /**
     * Apply the modifier to the given EffectEventResult.
     *
     * @param eventType the type of event that triggered the effect.
     * @param result    the EffectEventResult to which the modifier should be applied.
     */
    @Override
    public void applyModifier(EventType eventType, EffectEventResult result) {
        if (eventType != EventType.SHAMANIC_RITUAL) {
            throw new IllegalStateException("ShamanicImmunityModifier can only be applied to Shamanic events");
        }

        // This modifier makes the player immune to the negative effects of the Shamanic event, so we set any negative changes to 0
        if (result.getPpChange() < 0)
            result.setPpChange(0);
    }
}
