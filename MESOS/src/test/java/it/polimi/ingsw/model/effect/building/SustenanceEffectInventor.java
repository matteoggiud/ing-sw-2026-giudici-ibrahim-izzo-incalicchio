package it.polimi.ingsw.model.effect.building;

/**
 * For every Inventor you have a FoodDiscount
 */

public class SustenanceEffectInventor implements IBuildingEffect {
    private int FoodDiscount;
    @Override
    public int ApplyEffect(int N_Subject) {
        return N_Subject * FoodDiscount;
    }
}
