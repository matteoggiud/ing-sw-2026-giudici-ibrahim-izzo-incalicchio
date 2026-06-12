package it.polimi.ingsw.server.model.effect.building.modifiers;

import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.effect.event.ShamanicRitualEventEffect;

import java.util.Optional;

/**
 * You get Doubled PP from the Shamanic Event if you win the Shamanic Ritual.
 */
public class ShamanicDoubleBuildingEffect implements IBuildingEffect, IEventModifier {

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
     * by doubling the PP gain if the player has more shamanic stars than the others
     * @param eventCard the event card that triggered the event
     * @param result    the effect result that may be modified by this event modifier
     */
    @Override
    public void visitEvent(ShamanicRitualEventEffect eventCard, EffectEventResult result) {
        // If the player has more shamanic stars than the others, gain double the PP you would normally gain.
        if (result.getPpChange() > 0) {
            result.setPpChange(result.getPpChange() * 2);
        }
    }
}
