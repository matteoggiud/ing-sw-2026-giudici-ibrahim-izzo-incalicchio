package it.polimi.ingsw.Model.Card.Effects.BuildingEffect;

/*
For every character set you get a Food bonus
*/

public class AvatarEffectFood implements iBuildingEffect {
    int FoodGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return FoodGained * N_Subject;
    }
}
