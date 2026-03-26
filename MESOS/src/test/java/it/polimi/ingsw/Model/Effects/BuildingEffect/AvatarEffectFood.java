package it.polimi.ingsw.Model.Effects.BuildingEffect;

/*
For every character set you get a Food bonus
*/

public class AvatarEffectFood implements IBuildingEffect {
    private int FoodGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return FoodGained * N_Subject;
    }
}
