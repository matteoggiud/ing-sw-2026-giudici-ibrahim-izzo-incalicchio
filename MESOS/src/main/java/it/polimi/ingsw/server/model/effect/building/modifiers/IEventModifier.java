package it.polimi.ingsw.server.model.effect.building.modifiers;

import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.event.HuntEventEffect;
import it.polimi.ingsw.server.model.effect.event.RockPaintingEventEffect;
import it.polimi.ingsw.server.model.effect.event.ShamanicRitualEventEffect;
import it.polimi.ingsw.server.model.effect.event.SustenanceEventEffect;

/**
 * This interface represents a modifier for event effects.
 * It allows implementing classes to modify the effects of specific events
 */
public interface IEventModifier {

    /**
     * This method is called when a sustenance event occurs.
     * It uses visitor pattern
     *
     * @param eventCard the event card that triggered the event
     * @param result    the effect result that may be modified by this event modifier
     */
    default void visitEvent(SustenanceEventEffect eventCard, EffectEventResult result) {
    }

    /**
     * This method is called when a hunt event occurs.
     *
     * @param eventCard the event card that triggered the event
     * @param result    the effect result that may be modified by this event modifier
     */
    default void visitEvent(HuntEventEffect eventCard, EffectEventResult result) {
    }

    /**
     * This method is called when a shamanic ritual event occurs.
     *
     * @param eventCard the event card that triggered the event
     * @param result    the effect result that may be modified by this event modifier
     */
    default void visitEvent(ShamanicRitualEventEffect eventCard, EffectEventResult result) {
    }

    /**
     * This method is called when a rock painting event occurs.
     *
     * @param eventCard the event card that triggered the event
     * @param result    the effect result that may be modified by this event modifier
     */
    default void visitEvent(RockPaintingEventEffect eventCard, EffectEventResult result) {
    }
}
