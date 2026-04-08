package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;
import it.polimi.ingsw.model.card.CharacterType;

/**
 * Pick an additional card from the UpperRow after the end of the turn
 */

public class AdditionalCard implements IBuildingEffect {

    /**
     * Apply the effect of picking an additional card from the UpperRow after the end of the turn
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(0,0,0,1);
    }

    /**
     * Get the type of the building effect
     *
     * @return the type
     */
    @Override
    public CharacterType getSubjectType() {
        return null;
    }

    /**
     * Gets the event type associated with this building effect, which can be used to determine when the effect should be triggered during the game.
     *
     * @return the event type associated with this building effect
     */
    @Override
    public EventType getEventEffectType() {
        return null;
    }

}
