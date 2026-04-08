package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.card.CharacterType.ARTIST;

/**
 * If the artist count is below the required threshold,
 * lose a flat amount of PP.
 * If equals or above the threshold, gain PP for every Artist in the tribe
 */

public class RockPaintingEventEffect implements IEventEffect {

    /**
     * Applies the Rock Painting event effect to all players in the game.
     * Each player gains PP for each Artist in their tribe if they meet or exceed the threshold specified on the Event card; otherwise, they lose a flat amount of PP.
     *
     * @param game the current game state
     * @param eventCard the Event card that triggered the effect, used to determine the threshold and PP values
     * @return a list of EffectEventResult objects representing the changes to each player's resources
     */
    @Override
    public List<EffectEventResult> ApplyEffect(Game game, EventCard eventCard) {
        List<EffectEventResult> effectEventResults = new ArrayList<>();

        for (Player player : game.getPlayers().getShuffledPlayers()) {
            int artistCount = player.getPlayerDeck().getCountByType(ARTIST);

            if (artistCount >= eventCard.getThreshold()) { // Gain PP for each Artist
                effectEventResults.add(new EffectEventResult(0, artistCount * eventCard.getGainedPP(), player));
            } else { // Lose a flat amount of PP
                effectEventResults.add(new EffectEventResult(0, eventCard.getMalusPP(), player));
            }
        }

        return effectEventResults;
    }

    @Override
    public EventType getEventEffectType() {
        return EventType.ROCK_PAINTINGS;
    }
}
