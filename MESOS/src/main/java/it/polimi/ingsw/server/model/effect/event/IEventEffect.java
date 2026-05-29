package it.polimi.ingsw.server.model.effect.event;

import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.modifiers.IEventModifier;

import java.util.List;

/**
 * Interface representing the effect of an Event card in the game.
 * Implementing classes will define specific event effects and how they interact with modifiers and the game state.
 */
public interface IEventEffect {

    /**
     * Compute the base effect of the event card, without applying any modifiers.
     *
     * @param game      the current game state.
     * @param eventCard the event card for which the effect is being calculated.
     * @return a list of EffectEventResult representing the base effect of the event card.
     */
    List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard);

    /**
     * Post-process an effect result to apply event-specific refinement rules.
     * Default implementation does nothing; subclasses can override to add custom logic.
     *
     * @param change    the EffectEventResult to be refined.
     * @param eventCard the event card that triggered this effect.
     */
    default void postProcessChange(EffectEventResult change, EventCard eventCard) {
        // Default: no post-processing.
    }

    /**
     * Accept a visitor that can modify the effect result based on event-specific rules.
     *
     * @param eventModifier     the visitor that will apply modifications to the effect result.
     * @param effectEventResult the effect result that may be modified by the eventModifier.
     */
    void acceptModifier(IEventModifier eventModifier, EffectEventResult effectEventResult);

    /**
     * Defines the priority of the event effect for sorting purposes. Effects with higher priority values are applied after those with lower values.
     *
     * @return the sort priority of the event effect.
     */
    default int getSortPriority() {
        return 0;
    }
}
