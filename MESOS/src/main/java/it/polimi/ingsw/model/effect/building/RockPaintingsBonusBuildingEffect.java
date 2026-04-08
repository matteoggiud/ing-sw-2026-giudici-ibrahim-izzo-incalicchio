package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.modifiers.IEventModifier;
import it.polimi.ingsw.model.effect.modifiers.RockPaintingsBonusModifier;

import java.util.Optional;

/**
 * During rock paintings event, for each Artist that you have you gain a Food bonus
 */
public class RockPaintingsBonusBuildingEffect implements IBuildingEffect {
    private final IEventModifier modifier = new RockPaintingsBonusModifier();

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
