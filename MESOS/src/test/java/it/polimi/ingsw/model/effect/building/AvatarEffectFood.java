package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * For every character set you get a Food bonus
 */
public class AvatarEffectFood implements IBuildingEffect {

    /**
     * The amount of food gained by the player for each character set that they have
     */
    private int FoodGained;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(FoodGained * N_Subject,0,0,0);
    }
}
