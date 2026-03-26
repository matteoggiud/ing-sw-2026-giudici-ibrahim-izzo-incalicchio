package it.polimi.ingsw.model.effect.building;

/**
 * For every Hunter you gain a PP bonus
 */

public class PP_HunterEffect implements IBuildingEffect {
    private int PPGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return PPGained * N_Subject;
    }
}
