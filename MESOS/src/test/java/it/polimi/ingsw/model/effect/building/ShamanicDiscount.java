package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * During the Shamanic Event you don't lose PP
 */
public class ShamanicDiscount implements IBuildingEffect {

    /**
     * The amount of PP that the player doesn't lose during the Shamanic Event
     */
    private int PPDiscount;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(0,PPDiscount,0,0);
    }
}
