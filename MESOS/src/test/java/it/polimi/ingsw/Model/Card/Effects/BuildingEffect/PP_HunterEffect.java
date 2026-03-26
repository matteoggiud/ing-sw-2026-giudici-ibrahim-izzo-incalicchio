package it.polimi.ingsw.Model.Card.Effects.BuildingEffect;

/*
For every Hunter you gain a PP bonus
*/

public class PP_HunterEffect implements iBuildingEffect {
    int PPGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return PPGained * N_Subject;
    }
}
