package it.polimi.ingsw.Model.Effects.BuildingEffect;

/**
 * For every couple of Inventors you get FoodBonus
 * In N_Subjects goes (floor)N_Inventors/2
 */

public class DoubleInventorEffect implements IBuildingEffect {
    private int FoodGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return N_Subject*2;
    }
}
