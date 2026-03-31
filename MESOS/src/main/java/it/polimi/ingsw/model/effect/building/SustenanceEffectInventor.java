package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * For every Inventor you have a FoodDiscount
 */
public class SustenanceEffectInventor implements IBuildingEffect {

    /**
     * The amount of Food discounted for each Inventor that the player has
     */
    private int FoodDiscount;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(FoodDiscount * N_Subject, 0, 0, 0);
    }
}
