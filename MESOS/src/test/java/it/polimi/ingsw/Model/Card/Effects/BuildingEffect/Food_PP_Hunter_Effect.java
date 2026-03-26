package it.polimi.ingsw.Model.Card.Effects.BuildingEffect;

/*
You get a PP and Food bonus for every Hunter
*/

public class Food_PP_Hunter_Effect implements iBuildingEffect{
    int PPGained;
    int FoodGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return (PPGained + FoodGained) * N_Subject;
    }
}
