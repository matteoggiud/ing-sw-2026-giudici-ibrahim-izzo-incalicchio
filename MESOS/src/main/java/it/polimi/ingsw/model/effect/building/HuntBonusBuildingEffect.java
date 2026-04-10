package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.modifiers.IEventModifier;

import java.util.Optional;

/**
 * During the Hunt Event, for each Hunter in your tribe, gain 1 Food and 1 additional Prestige Point.
 */
public class HuntBonusBuildingEffect implements IBuildingEffect, IEventModifier {

    @Override
    public boolean isSupported(EventType eventType) {
        return eventType == EventType.HUNT;
    }

    /**
     * Apply the modifier to the given EffectEventResult.
     *
     * @param eventType the type of event that triggered the effect.
     * @param result    the EffectEventResult to which the modifier should be applied.
     */
    @Override
    public void applyModifier(EventType eventType, EffectEventResult result) {
        if (eventType != EventType.HUNT) {
            throw new IllegalStateException("HuntBonusModifier can only be applied to Hunt events");
        }

        Player p = result.getTargetPlayer();

        // Count the number of "Hunters" in the player's tribe
        int numbersOfHunters = p.getPlayerDeck().countCharactersByType(CharacterType.HUNTER);

        // For each Hunter, gain 1 Food and 1 additional Prestige Point
        result.setFoodChange(result.getFoodChange() + numbersOfHunters);
        result.setPpChange(result.getPpChange() + numbersOfHunters);
    }
}
