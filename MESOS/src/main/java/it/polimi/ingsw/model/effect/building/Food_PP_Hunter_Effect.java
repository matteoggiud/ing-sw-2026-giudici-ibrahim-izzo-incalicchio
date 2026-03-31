package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * You get a PP and Food bonus for every Hunter
 */
public class Food_PP_Hunter_Effect implements IBuildingEffect {

    /**
     * The amount of PP gained by the player for each hunter that they have
     */
    private int PPGained;

    /**
     * The amount of food gained by the player for each hunter that they have
     */
    private int FoodGained;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(FoodGained * N_Subject, PPGained * N_Subject, 0, 0);
    }
}
