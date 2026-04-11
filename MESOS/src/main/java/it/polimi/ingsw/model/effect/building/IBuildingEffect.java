package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.building.endgame.IEndGameScoring;
import it.polimi.ingsw.model.effect.building.modifiers.IEventModifier;
import it.polimi.ingsw.model.effect.building.triggers.IEventTrigger;

import java.util.Optional;

/**
 * This interface represents the effect of a building card in the game.
 * Each building card has a specific effect that can be applied during the game,
 * and this interface defines the method to apply that effect.
 */
public interface IBuildingEffect {


    /**
     * If the building modifies the input of shamanic stars calculation, it returns the number of bonus stars, otherwise it returns 0
     *
     * @return the number of bonus stars provided by the building.
     */
    default int getBonusShamanStars() {
        return 0;
    } //TODO: Understand whether to implement an IShamanStarsProvider interface for this method, so IBuildingEffect is cleaner

    /**
     * If the building implements IEventModifier it returns an Optional containing himself.
     *
     * @return an Optional containing the IEventModifier provided by this building effect.
     */
    default Optional<IEventModifier> getEventModifier() {
        return Optional.empty();
    }

    /**
     * If the building implements IEndGameScoring it returns an Optional containing himself.
     *
     * @return an Optional containing the IEndGameScoring provided by this building effect.
     */
    default Optional<IEndGameScoring> getEndGameScoring() {
        return Optional.empty();
    }

    /**
     * If the building implements IEventTrigger it returns an Optional containing himself.
     *
     * @return an Optional containing the IEventTrigger provided by this building effect.
     */
    default Optional<IEventTrigger> getEventTrigger() {
        return Optional.empty();
    }
}
