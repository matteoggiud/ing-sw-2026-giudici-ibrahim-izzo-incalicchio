package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.modifiers.HuntBonusModifier;
import it.polimi.ingsw.model.effect.modifiers.IEventModifier;

import java.util.Optional;

/**
 * During the Hunt Event, for each Hunter in your tribe, gain 1 Food and 1 additional Prestige Point.
 */
public class HuntBonusBuildingEffect implements IBuildingEffect {

    private final IEventModifier modifier = new HuntBonusModifier();

    /**
     * Returns the specific modifier associated with the Hunt Bonus Building Effect.
     *
     * @return an Optional containing the HuntBonusModifier.
     */
    @Override
    public Optional<IEventModifier> getEventModifier() {
        return Optional.of(modifier);
    }
}
