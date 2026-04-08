package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * For every couple of Inventors you get FoodBonus
 * In N_Subjects goes (floor)N_Inventors/2
 */
public class DoubleInventorEffect implements IBuildingEffect {

    /**
     * Apply the effect of gaining food based on the number of Inventors (N_Subject) that the player has
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(N_Subject/2,0,0,0);
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

    /**
     * Gets the event type associated with this building effect, which can be used to determine when the effect should be triggered during the game.
     *
     * @return the event type associated with this building effect
     */
    @Override
    public EventType getEventEffectType() {
        return null;
    }
}
