package it.polimi.ingsw.server.model.effect.building.modifiers;

import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.effect.event.HuntEventEffect;

import java.util.Optional;

/**
 * During the Hunt Event, for each Hunter in your tribe, gain 1 Food and 1 additional Prestige Point.
 */
public class HuntBonusBuildingEffect implements IBuildingEffect, IEventModifier {

    /**
     * This method returns an Optional containing this instance as an IEventModifier,
     * indicating that this building effect modifies the Hunt Event.
     * @return An Optional containing this instance as an IEventModifier.
     */
    @Override
    public Optional<IEventModifier> getEventModifier() {
        return Optional.of(this);
    }

    /**
     * This method modifies the effect of the Hunt Event
     * based on the number of Hunters in the player's tribe.
     * @param eventCard the event card that triggered the event
     * @param result    the effect result that may be modified by this event modifier
     */
    @Override
    public void visitEvent(HuntEventEffect eventCard, EffectEventResult result) {
        Player p = result.getTargetPlayer();

        // Count the number of "Hunters" in the player's tribe
        int numbersOfHunters = p.getPlayerDeck().countCharactersByType(CharacterType.HUNTER);

        // For each Hunter, gain 1 Food and 1 additional Prestige Point
        result.setFoodChange(result.getFoodChange() + numbersOfHunters);
        result.setPpChange(result.getPpChange() + numbersOfHunters);    }
}
