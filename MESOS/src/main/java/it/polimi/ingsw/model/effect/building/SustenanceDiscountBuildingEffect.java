package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.effect.modifiers.IEventModifier;
import it.polimi.ingsw.model.effect.modifiers.SustenanceDiscountModifier;

import java.util.Optional;

/**
 * During the Sustenance Event, you get a discount of 1 Food on the total
 * for each Character card of the specified type in your tribe (Artists/Inventors/Gatherers).
 */
public class SustenanceDiscountBuildingEffect implements IBuildingEffect {

    private final IEventModifier modifier;

    /**
     * Constructs a SustenanceDiscountBuildingEffect with the specified character type.
     *
     * @param characterType the type of character associated with this building effect.
     */
    public SustenanceDiscountBuildingEffect(CharacterType characterType) {
        modifier = new SustenanceDiscountModifier(characterType);
    }

    /**
     * Returns the specific modifier associated with the Sustenance Discount Building Effect.
     *
     * @return an Optional containing the SustenanceDiscountModifier.
     */
    @Override
    public Optional<IEventModifier> getEventModifier() {
        return Optional.of(modifier);
    }
}
