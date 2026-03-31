package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * For every couple of Inventors you get FoodBonus
 * In N_Subjects goes (floor)N_Inventors/2
 */
public class DoubleInventorEffect implements IBuildingEffect {
    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(N_Subject/2,0,0,0);
    }
}
