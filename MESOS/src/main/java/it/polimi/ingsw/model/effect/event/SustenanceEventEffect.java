package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.modifiers.IEventModifier;

import java.util.ArrayList;
import java.util.List;


/**
 * Pay 1 Food for every Character in the tribe. If Food is insufficient,
 * you lose the indicated PP for each unfed Character.
 * This event must always be resolved last
 */

public class SustenanceEventEffect implements IEventEffect {

    @Override
    public List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard) {
        List<EffectEventResult> effectEventResults = new ArrayList<>();

        for (Player player : game.getPlayers().getShuffledPlayers()) {
            // Calculate the base cost of the event for the player, which is equal to the number of Characters in their tribe
            int baseCost = player.getPlayerDeck().getCharacters().size();

            // Calculate the total gatherer discount for the player
            int gathererDiscount = player.getPlayerDeck().getTotalGathererDiscount();

            // Add the effect result for the player
            int foodCost = Math.max(baseCost - gathererDiscount, 0);
            effectEventResults.add(new EffectEventResult(-foodCost, 0, player));
        }

        return effectEventResults;
    }

    /**
     * Post-processes sustenance event changes to handle food deficits.
     * If a player cannot pay the full cost, they lose PP for each unfed character.
     *
     * @param change the EffectEventResult to be refined.
     * @param eventCard the sustenance event card.
     */
    @Override
    public void postProcessChange(EffectEventResult change, EventCard eventCard) {
        if (change.getFoodChange() < 0) {
            Player player = change.getTargetPlayer();

            // Calculate the total food deficit after applying the change
            int foodDeficit = player.getFood() + change.getFoodChange();

            if (foodDeficit < 0) {
                // If the player cannot pay the food cost, they pay all remaining food...
                change.setFoodChange(-player.getFood());

                // ...and lose PP for each unfed character, based on the malus indicated on the Event card
                int ppLoss = Math.abs(foodDeficit) * eventCard.getMalusPP();
                change.setPpChange(change.getPpChange() - ppLoss);
            }
        }
    }

    /**
     * @param eventModifier     the visitor that will apply modifications to the effect result.
     * @param effectEventResult the effect result that may be modified by the eventModifier.
     */
    @Override
    public void acceptModifier(IEventModifier eventModifier, EffectEventResult effectEventResult) {
        eventModifier.visitEvent(this, effectEventResult);
    }

    /**
     * Defines the priority of the sustenance event effect for sorting purposes. This effect must always be resolved last, so it has a higher priority value than other event effects.
     *
     * @return the sort priority of the sustenance event effect, which is set to 1 to ensure it is applied after other effects with the default priority of 0.
     */
    @Override
    public int getSortPriority() {
        return 1;
    }
}
