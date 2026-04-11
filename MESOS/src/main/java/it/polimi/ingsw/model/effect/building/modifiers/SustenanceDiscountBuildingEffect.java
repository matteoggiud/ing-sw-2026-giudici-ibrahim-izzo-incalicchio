package it.polimi.ingsw.model.effect.building.modifiers;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;

import java.util.List;
import java.util.Optional;

/**
 * During the Sustenance Event, you get a discount of 1 Food on the total
 * for each Character card of the specified type in your tribe (Artists/Inventors/Gatherers).
 */
public class SustenanceDiscountBuildingEffect implements IBuildingEffect, IEventModifier {

    /**
     * The valid character types for this building effect, which are Artists, Inventors, and Gatherers.
     */
    private static final List<CharacterType> VALID_CHARACTER_TYPES = List.of(CharacterType.ARTIST, CharacterType.INVENTOR, CharacterType.GATHERER);


    /**
     * The type of character associated with this building effect (e.g., Artists, Inventors, Gatherers).
     */
    private final CharacterType characterType;

    @Override
    public Optional<IEventModifier> getEventModifier() {
        return Optional.of(this);
    }

    /**
     * Constructs a SustenanceDiscountBuildingEffect with the specified character type.
     *
     * @param characterType the type of character associated with this building effect.
     */
    public SustenanceDiscountBuildingEffect(CharacterType characterType) {
        if (!VALID_CHARACTER_TYPES.contains(characterType))
            throw new IllegalArgumentException("Invalid character type");

        this.characterType = characterType;
    }


    // IEventModifier methods

    /**
     * Checks if the modifier is applicable to the given event type.
     *
     * @param eventType the type of event to check for applicability.
     * @return true if the modifier is applicable to the given event type, false otherwise.
     */
    @Override
    public boolean isSupported(EventType eventType) {
        return eventType == EventType.SUSTENANCE;
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
        result.setFoodChange(result.getFoodChange() + numbersOfCharacters);
    }
}

