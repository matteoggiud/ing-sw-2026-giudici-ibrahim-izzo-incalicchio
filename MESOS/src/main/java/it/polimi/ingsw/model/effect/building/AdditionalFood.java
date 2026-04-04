package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * At the end of the turn if your totem goes on a spot with a Food Bonus,
 * you get an additional Food otherwise the effect doesn't apply
 */

public class AdditionalFood implements IBuildingEffect {

    /**
     * The amount of food gained by the player if the effect is applied
     */
    private int FoodGained;

    /**
     * Constructs an AdditionalFood effect with the specified amount of food gained.
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(FoodGained,0,0,0);
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
}
