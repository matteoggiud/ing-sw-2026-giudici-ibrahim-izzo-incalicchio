package it.polimi.ingsw.server.model.effect.building.modifiers;

import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.effect.event.SustenanceEventEffect;

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


    @Override
    public void visitEvent(SustenanceEventEffect eventCard, EffectEventResult result) {
        Player p = result.getTargetPlayer();

        // Count the number of specified character type in the player's tribe
        int numbersOfCharacters = p.getPlayerDeck().countCharactersByType(characterType);

        // For each character of the specified type, apply a discount of 1 Food
        result.setFoodChange(result.getFoodChange() + numbersOfCharacters);
    }
}

