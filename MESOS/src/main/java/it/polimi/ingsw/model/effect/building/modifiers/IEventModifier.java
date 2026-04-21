package it.polimi.ingsw.model.effect.building.modifiers;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.event.HuntEventEffect;
import it.polimi.ingsw.model.effect.event.RockPaintingEventEffect;
import it.polimi.ingsw.model.effect.event.ShamanicRitualEventEffect;
import it.polimi.ingsw.model.effect.event.SustenanceEventEffect;

public interface IEventModifier {

    /**
     * Checks if the modifier is applicable to the given event type.
     *
     * @param eventType the type of event to check for applicability.
     * @return true if the modifier is applicable to the given event type, false otherwise.
     */
    @Deprecated
    boolean isSupported(EventType eventType);

    /**
     * Apply the modifier to the given EffectEventResult.
     *
     * @param eventType the type of event that triggered the effect.
     * @param result the EffectEventResult to which the modifier should be applied.
     */
    @Deprecated
    void applyModifier(EventType eventType, EffectEventResult result);

    default void visitEvent(SustenanceEventEffect eventCard, EffectEventResult result) {}
    default void visitEvent(HuntEventEffect eventCard, EffectEventResult result) {}
    default void visitEvent(ShamanicRitualEventEffect eventCard, EffectEventResult result) {}
    default void visitEvent(RockPaintingEventEffect eventCard, EffectEventResult result) {}
}
