package it.polimi.ingsw.model.effect.modifiers;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;

/**
 * During the rock paintings event, you gain 1 food for each Artist in your tribe.
 */
public class RockPaintingsBonusModifier implements IEventModifier {

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
