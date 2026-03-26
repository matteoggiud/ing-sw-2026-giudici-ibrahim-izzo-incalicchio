package it.polimi.ingsw.model.effect.building;

/**
 * For every Artist that you have you gain a Food bonus
 */

public class ArtistEffect implements IBuildingEffect {
    private int FoodGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return FoodGained * N_Subject;
    }
}
