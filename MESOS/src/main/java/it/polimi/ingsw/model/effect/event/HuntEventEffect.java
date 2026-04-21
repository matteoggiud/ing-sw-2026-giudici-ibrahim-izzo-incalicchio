package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.modifiers.IEventModifier;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.card.CharacterType.HUNTER;

/**
 * Each Hunter in the tribe generates 1 Food and the amount of Prestige Points indicated on the Event card
 */
public class HuntEventEffect implements IEventEffect {
    /**
     * Applies the particular Hunt event effect to all players in the game.
     *
     * @param game      the current game state
     * @param eventCard the Event card that triggered the effect, used to determine the amount of Prestige Points gained per Hunter
     * @return a list of EffectEventResult objects representing the changes to each player's resources
     */
    @Override
    public List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard) {
        List<EffectEventResult> effectEventResults = new ArrayList<>();

        // Loop through all players to apply the effect
        for (Player player : game.getPlayers().getShuffledPlayers()) {
            int hunterCount = player.getPlayerDeck().countCharactersByType(HUNTER);

            if (hunterCount > 0) { // Only add results for players who have at least one Hunter
                // Each Hunter generates 1 Food and the amount of Prestige Points indicated on the Event card
                effectEventResults.add(new EffectEventResult(hunterCount, hunterCount * eventCard.getGainedPP(), player));
            }
        }
        return effectEventResults;
    }

    /**
     * Accept a visitor that can modify the effect result based on event-specific rules.
     *
     * @param eventModifier     the visitor that will apply modifications to the effect result.
     * @param effectEventResult the effect result that may be modified by the eventModifier.
     */
    @Override
    public void accept(IEventModifier eventModifier, EffectEventResult effectEventResult) {
        eventModifier.visitEvent(this, effectEventResult);
    }

}
