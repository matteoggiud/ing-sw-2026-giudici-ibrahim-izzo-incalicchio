package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * For every Artist that you have you gain a Food bonus
 */
public class ArtistEffect implements IBuildingEffect {

    /**
     * The amount of food gained by the player for each artist that they have
     */
    private int FoodGained;

    @Override
    public EffectResult ApplyEffect(int N_Subject) {
        return new EffectResult(FoodGained * N_Subject,0,0,0);
    }
}
