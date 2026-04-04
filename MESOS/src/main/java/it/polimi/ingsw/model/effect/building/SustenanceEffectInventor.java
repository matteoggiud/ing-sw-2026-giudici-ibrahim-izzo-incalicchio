package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * For every Inventor you have a FoodDiscount
 */
public class SustenanceEffectInventor implements IBuildingEffect {

    /**
     * The amount of Food discounted for each Inventor that the player has
     */
    private int FoodDiscount;

    /**
     * Apply the effect of discounting food based on the number of Inventor (N_Subject) that the player has
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(FoodDiscount * N_Subject, 0, 0, 0);
    }

    /**
     * Give the type of character that this effect is related to
     *
     * @return type of character
     */
    @Override
    public CharacterType getSubjectType() {
        return CharacterType.INVENTOR;
    }
}
