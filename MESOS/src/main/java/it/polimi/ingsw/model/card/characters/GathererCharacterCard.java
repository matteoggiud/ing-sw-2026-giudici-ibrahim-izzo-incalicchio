package it.polimi.ingsw.model.card.characters;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.Era;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.modifiers.IEventModifier;

/**
 * Represents a Gatherer Character Card in the game.
 */
public class GathererCharacterCard extends CharacterCard implements IEventModifier {
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
        if (eventType == EventType.SUSTENANCE) {
            // Increase the food change by 3 (which effectively reduces the food loss by 3)
            result.setFoodChange(result.getFoodChange() + 3);

            // Ensure that the food change does not become positive
            if (result.getFoodChange() > 0) {
                result.setFoodChange(0);
            }
        }
    }

    /**
     * Constructs a new GathererCharacterCard with the specified attributes.
     *
     * @param name          the name of the card
     * @param era           the era of the card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @throws IllegalArgumentException if numeric constraints are violated or if something is null
     */
    public GathererCharacterCard(String name, Era era, int minNumPlayers) {
        super(name, era, minNumPlayers, CharacterType.GATHERER);
    }
}
