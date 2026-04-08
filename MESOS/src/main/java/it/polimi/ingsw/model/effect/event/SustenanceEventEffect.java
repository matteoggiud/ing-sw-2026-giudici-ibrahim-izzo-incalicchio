package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.deck.EventDiscount;
import it.polimi.ingsw.model.effect.EffectEventResult;

import java.util.ArrayList;
import java.util.List;


/**
 * Pay 1 Food for every Character in the tribe. If Food is insufficient,
 * you lose the indicated PP for each unfed Character.
 * This event must always be resolved last
 */

public class SustenanceEventEffect implements IEventEffect {

    @Override
    public EventType getEventEffectType() {
        return EventType.SUSTENANCE;
    }

    @Override
    public List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard) {
        List<EffectEventResult> effectEventResults = new ArrayList<>();

        for (Player player : game.getPlayers().getShuffledPlayers()) {
            // Calculate the base cost of the event for the player, which is equal to the number of Characters in their tribe
            int baseCost = player.getPlayerDeck().getCharacters().size();

            // Add the effect result for the player
            effectEventResults.add(new EffectEventResult(-baseCost, 0, player));
        }

        return effectEventResults;
    }
}
