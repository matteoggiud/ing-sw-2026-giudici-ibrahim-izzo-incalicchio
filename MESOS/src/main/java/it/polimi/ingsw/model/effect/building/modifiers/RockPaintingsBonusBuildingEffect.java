package it.polimi.ingsw.model.effect.building.modifiers;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;

/**
 * During rock paintings event, for each Artist that you have you gain a Food bonus
 */
public class RockPaintingsBonusBuildingEffect implements IBuildingEffect, IEventModifier {

    @Override
    public boolean isSupported(EventType eventType) {
        return eventType == EventType.ROCK_PAINTINGS;
    }

    /**
     * Apply the modifier to the given EffectEventResult.
     *
     * @param eventType the type of event that triggered the effect.
     * @param result    the EffectEventResult to which the modifier should be applied.
     */
    @Override
    public void applyModifier(EventType eventType, EffectEventResult result) {
        if (eventType != EventType.ROCK_PAINTINGS) {
            throw new IllegalStateException("RockPaintingsBonusModifier can only be applied to Exploration events");
        }

        Player p = result.getTargetPlayer();

        // Count the number of "Artists" in the player's tribe
        int numbersOfArtists = p.getPlayerDeck().countCharactersByType(CharacterType.ARTIST);

        // For each Artist, gain 1 Food
        result.setFoodChange(result.getFoodChange() + numbersOfArtists);

    }
}
