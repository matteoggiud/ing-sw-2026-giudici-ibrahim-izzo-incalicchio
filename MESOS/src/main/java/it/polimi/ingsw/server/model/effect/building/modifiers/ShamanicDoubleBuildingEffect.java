package it.polimi.ingsw.server.model.effect.building.modifiers;

import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.effect.event.ShamanicRitualEventEffect;

import java.util.Optional;

/**
 * You get Doubled PP from the Shamanic Event if you win the Shamanic Ritual.
 */
public class ShamanicDoubleBuildingEffect implements IBuildingEffect, IEventModifier {

    @Override
    public Optional<IEventModifier> getEventModifier() {
        return Optional.of(this);
    }


    @Override
    public void visitEvent(ShamanicRitualEventEffect eventCard, EffectEventResult result) {
        // If the player has more shamanic stars than the others, gain double the PP you would normally gain.
        if (result.getPpChange() > 0) {
            result.setPpChange(result.getPpChange() * 2);
        }
    }
}
