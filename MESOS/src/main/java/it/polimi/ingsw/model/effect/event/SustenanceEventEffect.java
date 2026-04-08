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
    public List<EffectEventResult> ApplyEffect(Game game, EventCard eventCard) {
        List<EffectEventResult> effectEventResults = new ArrayList<>();

        for (Player player : game.getPlayers().getShuffledPlayers()) {
            EventDiscount discount = player.getPlayerDeck().calculateTotalDiscount(getEventEffectType()); // Calculate the discount provided by gatherer cards plus building cards
            int toPay = Math.max(player.getPlayerDeck().getCharacters().size() - discount.getFood(), 0); // Calculate the total food cost after applying discounts

            if (player.getFood() < toPay) { // If the player doesn't have enough food to pay for all characters
                int ppLoss = (toPay - player.getFood()) * eventCard.getMalusPP(); // Calculate the PP loss for unfed characters
                effectEventResults.add(new EffectEventResult(-player.getFood(), ppLoss, player)); // Player pays all their food and loses PP for unfed characters

            } else {
                effectEventResults.add(new EffectEventResult(-toPay, 0, player));
            }
        }

        return effectEventResults;
    }
}
