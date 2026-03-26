package it.polimi.ingsw.Model.Card.Effects.BuildingEffect;

/*
For every Gathered you have a FoodDiscount
*/

public class SustenanceEffectGathered implements iBuildingEffect {
    int FoodDiscount;
    @Override
    public int ApplyEffect(int N_Subject) {
        return FoodDiscount * N_Subject;
    }
}
