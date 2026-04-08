package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.modifiers.IEventModifier;
import it.polimi.ingsw.model.effect.modifiers.ShamanicDoubleModifier;

import java.util.Optional;

/**
 * You get Doubled PP from the Shamanic Event if you win the Shamanic Ritual.
 */
public class ShamanicDoubleBuildingEffect implements IBuildingEffect {
    private final IEventModifier modifier = new ShamanicDoubleModifier();

    /**
     * If the building modifies the event, it returns his modifier, otherwise it returns an empty Optional
     *
     * @return an Optional containing the IEventModifier if the building modifies the event, or an empty Optional if it does not
     */
    @Override
    public Optional<IEventModifier> getEventModifier() {
        return Optional.of(modifier);
    }
}
