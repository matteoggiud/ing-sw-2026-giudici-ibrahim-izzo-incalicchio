package it.polimi.ingsw.Model.Effects.BuildingEffect;

/*
You get Doubled PP from the Shamanic Event if you win
In N_Subject goes the N_Stars of the player
*/

public class ShamanicDouble implements IBuildingEffect {
    @Override
    public int ApplyEffect(int N_Subject) {
        return N_Subject * 2;
    }
}
