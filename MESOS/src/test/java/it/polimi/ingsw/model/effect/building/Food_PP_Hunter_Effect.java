package it.polimi.ingsw.model.effect.building;

/**
 * You get a PP and Food bonus for every Hunter
 */

public class Food_PP_Hunter_Effect implements IBuildingEffect {
    private int PPGained;
    private int FoodGained;
    @Override
    public int ApplyEffect(int N_Subject) {
        return (PPGained + FoodGained) * N_Subject;
    }
}
