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

    @Override
    public void visitEvent(ShamanicRitualEventEffect eventCard, EffectEventResult result) {
        // This modifier makes the player immune to the negative effects of the Shamanic event, so we set any negative changes to 0
        if (result.getPpChange() < 0)
            result.setPpChange(0);
    }
}
