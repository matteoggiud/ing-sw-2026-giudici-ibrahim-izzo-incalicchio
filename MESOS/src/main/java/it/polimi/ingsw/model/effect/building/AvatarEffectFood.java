package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * For every character set you get a Food bonus
 */
public class AvatarEffectFood implements IBuildingEffect {

    /**
     * The amount of food gained by the player for each character set that they have
     */
    private int FoodGained;

    /**
     * Apply the effect of gaining food based on the number of character sets (N_Subject) that the player has
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(FoodGained * N_Subject,0,0,0);
    }

    /**
     * Give the type of character that this effect is related to
     *
     * @return type of character
     */
    @Override
    public CharacterType getSubjectType() {
        return null;
    }

    @Override
    public EventType getEventEffectType() {
        return null;
    }
}
