package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.modifiers.IEventModifier;

import java.util.Optional;

/**
 * This interface represents the effect of a building card in the game.
 * Each building card has a specific effect that can be applied during the game,
 * and this interface defines the method to apply that effect.
 */
public interface IBuildingEffect {

    /**
     * If the building modifies the event, it returns his modifier, otherwise it returns an empty Optional
     *
     * @return an Optional containing the IEventModifier if the building modifies the event, or an empty Optional if it does not
     */
    default Optional<IEventModifier> getEventModifier() {
        return Optional.empty();
    }

    /**
     * If the building modifies the input of shamanic stars calculation, it returns the number of bonus stars, otherwise it returns 0
     *
     * @return the number of bonus stars provided by the building.
     */
    default int getBonusShamanStars() {
        return 0;
    }

    //TODO: add the method to get the end game scoring
    default Optional<IEndGameScoring> getEndGameScoring() {
        return Optional.empty();
    }

    //TODO: add the method to get the trigger effect (observer pattern)
    // default Optional<ITriggerEffect> getTriggerEffect() { return Optional.empty(); }


}
