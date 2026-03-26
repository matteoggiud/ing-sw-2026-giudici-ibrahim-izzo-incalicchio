package it.polimi.ingsw.model.effect.building;

/**
 * For every character set you get a PP bonus
 */

public class AvatarEffectPP implements IBuildingEffect {
    private int PPGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return N_Subject * PPGained;
    }
}
