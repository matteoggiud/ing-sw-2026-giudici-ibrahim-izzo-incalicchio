package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * During the Shamanic Event you don't lose PP
 */
public class ShamanicDiscount implements IBuildingEffect {

    /**
     * The amount of PP that the player doesn't lose during the Shamanic Event
     */
    private int PPDiscount;

    /**
     * Apply the effect of not losing PP during the Shamanic Event based on the number of PP that the player would lose (N_Subject)
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(0,PPDiscount,0,0);
    }

    /**
     * Give the type of character that this effect is related to
     *
     * @return type of character
     */
    @Override
    public CharacterType getSubjectType() {
        return CharacterType.SHAMAN;
    }
}
