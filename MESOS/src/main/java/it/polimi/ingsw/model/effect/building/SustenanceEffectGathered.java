package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * For every Gathered you have a FoodDiscount
 */
public class SustenanceEffectGathered implements IBuildingEffect {

    /**
     * The amount of Food discounted for each Gathered that the player has
     */
    private int FoodDiscount;

    /**
     * Apply the effect of discounting food based on the number of Gathered (N_Subject) that the player has
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(FoodDiscount * N_Subject,0,0,0);
    }

    /**
     * Give the type of character that this effect is related to
     *
     * @return type of character
     */
    @Override
    public CharacterType getSubjectType() {
        return CharacterType.GATHERER;
    }

    /**
     * Gets the event type associated with this building effect, which can be used to determine when the effect should be triggered during the game.
     *
     * @return the event type associated with this building effect
     */
    @Override
    public EventType getEventEffectType() {
        return EventType.SUSTENANCE;
    }
}
