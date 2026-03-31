package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * During the Shamanic Event you get the additional Stars in the Building
 */
public class ShamanicStarAddition implements IBuildingEffect {

    /**
     * The amount of Stars that the player gets during the Shamanic Event
     */
    private int Star;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(0,0,Star,0);
    }
}
