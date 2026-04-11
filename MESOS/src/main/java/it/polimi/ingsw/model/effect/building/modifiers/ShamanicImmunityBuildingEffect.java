package it.polimi.ingsw.model.effect.building.modifiers;

import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;

import java.util.Optional;

/**
 * During the Shamanic Event you don't lose PP
 */
public class ShamanicImmunityBuildingEffect implements IBuildingEffect, IEventModifier {

    @Override
    public Optional<IEventModifier> getEventModifier() {
        return Optional.of(this);
    }

    // IEventModifiers methods
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
        if (!isSupported(eventType)) {
            throw new IllegalStateException("ShamanicImmunityModifier can only be applied to Shamanic events");
        }

        // This modifier makes the player immune to the negative effects of the Shamanic event, so we set any negative changes to 0
        if (result.getPpChange() < 0)
            result.setPpChange(0);
    }
}
