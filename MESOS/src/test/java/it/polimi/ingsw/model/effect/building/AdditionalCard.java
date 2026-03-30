package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * Pick an additional card from the UpperRow after the end of the turn
 */

public class AdditionalCard implements IBuildingEffect {
    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(0,0,0,1);
    }
}
