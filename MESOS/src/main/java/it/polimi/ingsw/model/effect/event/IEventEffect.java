package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.modifiers.IEventModifier;

import java.util.List;

public interface IEventEffect {

    /**
     * Compute the base effect of the event card, without applying any modifiers.
     *
     * @param game the current game state.
     * @param eventCard the event card for which the effect is being calculated.
     * @return a list of EffectEventResult representing the base effect of the event card.
     */
    List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard);

    /**
     * Post-process an effect result to apply event-specific refinement rules.
     * Default implementation does nothing; subclasses can override to add custom logic.
     *
     * @param change the EffectEventResult to be refined.
     * @param eventCard the event card that triggered this effect.
     */
    default void postProcessChange(EffectEventResult change, EventCard eventCard) {
        // Default: no post-processing.
    }

    /**
     * Accept a visitor that can modify the effect result based on event-specific rules.
     *
     * @param eventModifier the visitor that will apply modifications to the effect result.
     * @param effectEventResult the effect result that may be modified by the eventModifier.
     */
    void accept(IEventModifier eventModifier, EffectEventResult effectEventResult);
}
