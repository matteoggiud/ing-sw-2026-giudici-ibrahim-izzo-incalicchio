package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;

import java.util.ArrayList;
import java.util.List;

/**
 * The player with the most star icons gains PP,
 * while the player with the least loses PP.
 * In case of a tie, all tied players gain or lose the points
 */

public class ShamanicRitualEventEffect implements IEventEffect {

    @Override
    public EventType getEventEffectType() {
        return EventType.SHAMANIC_RITUAL;
    }

    @Override
    public List<EffectEventResult> ApplyEffect(Game game, EventCard eventCard) {
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
                leastStarPlayers.add(new EffectEventResult(0, eventCard.getMalusPP(), player)); // Player loses PP for having the least stars
            } else if (starCount == minStars) {
                leastStarPlayers.add(new EffectEventResult(0, eventCard.getMalusPP(), player)); // Player also loses PP if tied for least stars
            }
        }

        effectEventResults.addAll(mostStarPlayers);
        effectEventResults.addAll(leastStarPlayers);

        return effectEventResults;
    }
}
