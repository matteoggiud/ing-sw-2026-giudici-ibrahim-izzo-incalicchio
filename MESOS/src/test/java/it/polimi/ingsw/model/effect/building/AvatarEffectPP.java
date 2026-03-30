package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * For every character set you get a PP bonus
 */
public class AvatarEffectPP implements IBuildingEffect {

    /**
     * The amount of PP gained by the player for each character set that they have
     */
    private int PPGained;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(0,N_Subject * PPGained,0,0);
    }
}
