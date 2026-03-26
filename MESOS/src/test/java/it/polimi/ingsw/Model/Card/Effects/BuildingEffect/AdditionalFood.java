package it.polimi.ingsw.Model.Card.Effects.BuildingEffect;

/*
At the end of the turn if your totem goes on a spot with a Food Bonus,
you get an additional Food otherwise the effect doesn't apply
*/

public class AdditionalFood implements iBuildingEffect {
    int PPGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return 0;
    }
}
