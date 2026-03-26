package it.polimi.ingsw.Model.Effects.BuildingEffect;

/**
 * At the end of the match you get doubled the PP of all the Builders
 * In N_Subject goes all the PP given by the Builders
 */

public class BuildersEffect implements IBuildingEffect {
    private int PPGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return PPGained * N_Subject;
    }
}
