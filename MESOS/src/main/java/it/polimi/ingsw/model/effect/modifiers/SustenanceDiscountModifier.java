package it.polimi.ingsw.model.effect.modifiers;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;

import java.util.ArrayList;
import java.util.List;

/**
 * During the Sustenance Event, you get a discount of 1 Food on the total
 * for each Character card of the specified type in your tribe (Artists/Inventors/Gatherers).
 */
public class SustenanceDiscountModifier implements IEventModifier {
    private final CharacterType characterType;

    public SustenanceDiscountModifier(CharacterType characterType) {
        List<CharacterType> validCharacterTypes = new ArrayList<>(List.of(CharacterType.HUNTER, CharacterType.GATHERER, CharacterType.ARTIST));

        if (characterType == null || !validCharacterTypes.contains(characterType)) {
            throw new IllegalArgumentException("Invalid character type");
        }
        this.characterType = characterType;
    }

    /**
     * Apply the modifier to the given EffectEventResult.
     *
     * @param eventType the type of event that triggered the effect.
     * @param result    the EffectEventResult to which the modifier should be applied.
     */
    @Override
    public void applyModifier(EventType eventType, EffectEventResult result) {
        if (eventType != EventType.SUSTENANCE) {
            throw new IllegalStateException("SustenanceDiscountModifier can only be applied to Sustenance events");
        }

        Player p = result.getTargetPlayer();

        // Count the number of specified character type in the player's tribe
        int numbersOfCharacters = p.getPlayerDeck().countCharactersByType(characterType);

        // For each character of the specified type, apply a discount of 1 Food
        result.setFoodChange(result.getFoodChange() - numbersOfCharacters);
    }
}
