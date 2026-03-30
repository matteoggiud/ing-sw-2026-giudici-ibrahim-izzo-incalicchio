package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * At the end of the turn if your totem goes on a spot with a Food Bonus,
 * you get an additional Food otherwise the effect doesn't apply
 */

public class AdditionalFood implements IBuildingEffect {

    /**
     * The amount of food gained by the player if the effect is applied
     */
    private int FoodGained;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(FoodGained,0,0,0);
    }
}
