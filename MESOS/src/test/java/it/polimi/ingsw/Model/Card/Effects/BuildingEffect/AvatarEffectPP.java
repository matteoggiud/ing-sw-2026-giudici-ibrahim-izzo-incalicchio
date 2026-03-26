package it.polimi.ingsw.Model.Card.Effects.BuildingEffect;

/*
For every character set you get a PP bonus
*/

public class AvatarEffectPP implements iBuildingEffect{
    int PPGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return N_Subject * PPGained;
    }
}
