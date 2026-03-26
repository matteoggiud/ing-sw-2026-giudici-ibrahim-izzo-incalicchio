package it.polimi.ingsw.Model.Effects.BuildingEffect;

/*
For every Gathered you have a FoodDiscount
*/

public class SustenanceEffectGathered implements IBuildingEffect {
    private int FoodDiscount;
    @Override
    public int ApplyEffect(int N_Subject) {
        return FoodDiscount * N_Subject;
    }
}
