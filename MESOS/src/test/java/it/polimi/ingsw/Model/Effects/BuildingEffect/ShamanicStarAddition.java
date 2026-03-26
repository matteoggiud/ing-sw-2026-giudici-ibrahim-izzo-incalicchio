package it.polimi.ingsw.Model.Effects.BuildingEffect;

/*
During the Shamanic Event you get the additional Stars in the Building
*/

public class ShamanicStarAddition implements IBuildingEffect {
    private int Star;
    @Override
    public int ApplyEffect(int N_Subject) {
        return Star;
    }
}
