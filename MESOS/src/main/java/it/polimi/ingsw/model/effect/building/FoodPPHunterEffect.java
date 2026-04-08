package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * You get a PP and Food bonus for every Hunter
 */
public class FoodPPHunterEffect implements IBuildingEffect {

    /**
     * The amount of PP gained by the player for each hunter that they have
     */
    private int PPGained;

    /**
     * The amount of food gained by the player for each hunter that they have
     */
    private int FoodGained;

    /**
     * Apply the effect of gaining PP and food based on the number of hunters (N_Subject) that the player has
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(FoodGained * N_Subject, PPGained * N_Subject, 0, 0);
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

    /**
     * Gets the event type associated with this building effect, which can be used to determine when the effect should be triggered during the game.
     *
     * @return the event type associated with this building effect
     */
    @Override
    public EventType getEventEffectType() {
        return EventType.HUNT;
    }
}
