package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.building.endgame.IEndGameScoring;
import it.polimi.ingsw.model.effect.building.extraction.IExtraActionEffect;
import it.polimi.ingsw.model.effect.building.modifiers.IEventModifier;
import it.polimi.ingsw.model.effect.building.shamanicstars.IShamanicStarsProvider;
import it.polimi.ingsw.model.effect.building.triggers.IEventTrigger;

import java.util.Optional;

/**
 * This interface represents the effect of a building card in the game.
 * Each building card has a specific effect that can be applied during the game,
 * and this interface defines the method to apply that effect.
 */
public interface IBuildingEffect {




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

    /**
     * If the building implements IShamanicStarsProvider it returns an Optional containing himself.
     *
     * @return an Optional containing the IShamanicStarsProvider provided by this building effect.
     */
    default Optional<IShamanicStarsProvider>  getShamanicStarsProvider() {
        return Optional.empty();
    }

    /**
     * If the building implements IExtraActionEffect it returns an Optional containing himself.
     *
     * @return an Optional containing the IExtraActionEffect provided by this building effect.
     */
    default Optional<IExtraActionEffect> getExtraActionEffect() {
        return Optional.empty();
    }
}
