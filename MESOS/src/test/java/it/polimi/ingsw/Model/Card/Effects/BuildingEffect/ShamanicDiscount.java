package it.polimi.ingsw.Model.Card.Effects.BuildingEffect;

/*
During the Shamanic Event you don't lose PP
*/

public class ShamanicDiscount implements iBuildingEffect {
    int PPDiscount;
    @Override
    public int ApplyEffect(int N_Subject) {
        return PPDiscount;
    }
}
