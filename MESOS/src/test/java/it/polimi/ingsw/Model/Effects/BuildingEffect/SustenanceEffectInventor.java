package it.polimi.ingsw.Model.Effects.BuildingEffect;

/*
For every Inventor you have a FoodDiscount
*/

public class SustenanceEffectInventor implements IBuildingEffect {
    int FoodDiscount;
    @Override
    public int ApplyEffect(int N_Subject) {
        return N_Subject * FoodDiscount;
    }
}
