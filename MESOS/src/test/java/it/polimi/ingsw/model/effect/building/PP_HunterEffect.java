package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * For every Hunter you gain a PP bonus
 */
public class PP_HunterEffect implements IBuildingEffect {

    /**
     * The amount of PP gained by the player for each hunter that they have
     */
    private int PPGained;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(0,PPGained * N_Subject,0,0);
    }
}
