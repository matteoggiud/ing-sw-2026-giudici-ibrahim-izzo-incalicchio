package it.polimi.ingsw.Model.Card.Effects.BuildingEffect;

/*
During the Shamanic Event you get the additional Stars in the Building
*/

public class ShamanicStarAddition implements iBuildingEffect{
    int Star;
    @Override
    public int ApplyEffect(int N_Subject) {
        return Star;
    }
}
