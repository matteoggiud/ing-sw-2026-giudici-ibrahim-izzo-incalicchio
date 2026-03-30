package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * You get Doubled PP from the Shamanic Event if you win
 * In N_Subject goes the N_Stars of the player
 */
public class ShamanicDouble implements IBuildingEffect {
    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(0,N_Subject*2, 0,0);
    }
}
