package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;
import it.polimi.ingsw.model.effect.modifiers.IEventModifier;
import it.polimi.ingsw.model.effect.modifiers.ShamanicImmunityModifier;

import java.util.Optional;

/**
 * During the Shamanic Event you don't lose PP
 */
public class ShamanicImmunityBuildingEffect implements IBuildingEffect {
    private final IEventModifier modifier = new ShamanicImmunityModifier();


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
