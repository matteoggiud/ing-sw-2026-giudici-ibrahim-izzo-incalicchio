package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * For every Hunter you gain a PP bonus
 */
public class PP_HunterEffect implements IBuildingEffect {

    /**
     * The amount of PP gained by the player for each hunter that they have
     */
    private int PPGained;

    /**
     * Apply the effect of gaining PP based on the number of hunters (N_Subject) that the player has
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(0,PPGained * N_Subject,0,0);
    }

    /**
     * Give the type of character that this effect is related to
     *
     * @return type of character
     */
    @Override
    public CharacterType getSubjectType() {
        return CharacterType.HUNTER;
    }
}
