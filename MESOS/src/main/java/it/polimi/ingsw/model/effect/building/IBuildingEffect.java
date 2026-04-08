package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * This interface represents the effect of a building card in the game.
 * Each building card has a specific effect that can be applied during the game,
 * and this interface defines the method to apply that effect.
 */
public interface IBuildingEffect {

    /**
     * Apply the effect of the building card based on the number of subjects (N_Subject) that are relevant to the effect.
     * @param N_Subject
     * @return
     */
    EffectBuildingResult ApplyEffect(int N_Subject);

    /**
     * Get the type of the building effect, which can be used to determine how the effect should be applied or displayed in the game.
     * @return
     */
    CharacterType getSubjectType();

    /**
     * Gets the event type associated with this building effect, which can be used to determine when the effect should be triggered during the game.
     *
     * @return the event type associated with this building effect
     */
    EventType getEventEffectType();
}
