package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * At the end of the match you get doubled the PP of all the Builders
 * In N_Subject goes all the PP given by the Builders
 */
public class BuildersEffect implements IBuildingEffect {

    /**
     * The amount of PP gained by the player for each PP given by the Builders
     */
    private int PPGained;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(PPGained * N_Subject,0,0,0);
    }
}
