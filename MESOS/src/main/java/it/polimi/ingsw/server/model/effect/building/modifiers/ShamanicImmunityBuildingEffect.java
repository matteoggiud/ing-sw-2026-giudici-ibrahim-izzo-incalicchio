package it.polimi.ingsw.server.model.effect.building.modifiers;

import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.effect.event.ShamanicRitualEventEffect;

import java.util.Optional;

/**
 * During the Shamanic Event you don't lose PP
 */
public class ShamanicImmunityBuildingEffect implements IBuildingEffect, IEventModifier {

    /**
     * This method returns an Optional containing this instance as an IEventModifier,
     * indicating that this building effect modifies the Shamanic Ritual Event.
     * @return An Optional containing this instance as an IEventModifier.
     */
    @Override
    public Optional<IEventModifier> getEventModifier() {
        return Optional.of(this);
    }

    /**
     * This method modifies the effect of the Shamanic Ritual Event
     * by making the player immune to any negative PP changes.
     * @param eventCard the event card that triggered the event
     * @param result    the effect result that may be modified by this event modifier
     */
    @Override
    public void visitEvent(ShamanicRitualEventEffect eventCard, EffectEventResult result) {
        // This modifier makes the player immune to the negative effects of the Shamanic event, so we set any negative changes to 0
        if (result.getPpChange() < 0)
            result.setPpChange(0);
    }
}
