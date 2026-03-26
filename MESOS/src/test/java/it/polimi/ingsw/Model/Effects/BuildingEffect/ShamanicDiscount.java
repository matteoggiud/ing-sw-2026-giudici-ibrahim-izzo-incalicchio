package it.polimi.ingsw.Model.Effects.BuildingEffect;

/*
During the Shamanic Event you don't lose PP
*/

public class ShamanicDiscount implements IBuildingEffect {
    private int PPDiscount;
    @Override
    public int ApplyEffect(int N_Subject) {
        return PPDiscount;
    }
}
