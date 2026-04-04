package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * You get Doubled PP from the Shamanic Event if you win
 * In N_Subject goes the N_Stars of the player
 */
public class ShamanicDouble implements IBuildingEffect {

    /**
     * Apply the effect of gaining doubled PP from the Shamanic Event based on the number of stars (N_Subject) that the player has
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(0,N_Subject*2, 0,0);
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
