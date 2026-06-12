package it.polimi.ingsw.server.model.effect.event;

import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.modifiers.IEventModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * The player with the most star icons gains PP,
 * while the player with the least loses PP.
 * In case of a tie, all tied players gain or lose the points
 */

public class ShamanicRitualEventEffect implements IEventEffect {
    /**
     * Accept a visitor that can modify the effect result based on event-specific rules.
     * @param eventModifier     the visitor that will apply modifications to the effect result.
     * @param effectEventResult the effect result that may be modified by the eventModifier.
     */
    @Override
    public void acceptModifier(IEventModifier eventModifier, EffectEventResult effectEventResult) {
        eventModifier.visitEvent(this, effectEventResult);

    }

    /**
     * Calculates the base effect of the Shamanic Ritual event card by determining which player(s)
     * have the most and least star icons in their tribe.
     * Players with the most star icons gain PP, while players with the least star icons lose PP.
     * In case of a tie for either category, all tied players are affected accordingly.
     * @param game      the current game state.
     * @param eventCard the event card for which the effect is being calculated.
     * @return a list of EffectEventResult representing the base effect of the Shamanic Ritual event card,
     * including PP gains and losses for the affected players.
     */
    @Override
    public List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard) {
        List<EffectEventResult> effectEventResults = new ArrayList<>();

        // Find the list of players with the most and least star icons
        List<EffectEventResult> mostStarPlayers = new ArrayList<>();
        List<EffectEventResult> leastStarPlayers = new ArrayList<>();
        int maxStars = Integer.MIN_VALUE;
        int minStars = Integer.MAX_VALUE;

        for (Player player : game.getPlayers().getShuffledPlayers()) {
            int starCount = player.getPlayerDeck().getTotalShamanStars(); // Calculate the total number of star icons for the player

            if (starCount > maxStars) {
                maxStars = starCount;
                mostStarPlayers.clear();
                mostStarPlayers.add(new EffectEventResult(0, eventCard.getGainedPP(), player)); // Player gains PP for having the most stars
            } else if (starCount == maxStars) {
                mostStarPlayers.add(new EffectEventResult(0, eventCard.getGainedPP(), player)); // Player also gains PP if tied for most stars
            }
            if (starCount < minStars) {
                minStars = starCount;
                leastStarPlayers.clear();
                leastStarPlayers.add(new EffectEventResult(0, -eventCard.getMalusPP(), player)); // Player loses PP for having the least stars
            } else if (starCount == minStars) {
                leastStarPlayers.add(new EffectEventResult(0, -eventCard.getMalusPP(), player)); // Player also loses PP if tied for least stars
            }
        }

        effectEventResults.addAll(mostStarPlayers);
        effectEventResults.addAll(leastStarPlayers);

        return effectEventResults;
    }
}
