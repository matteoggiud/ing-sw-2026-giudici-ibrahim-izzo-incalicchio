package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.modifiers.IEventModifier;

import java.util.List;
import java.util.Stack;

/**
 * The EventResolver class is responsible for resolving the effects of event cards in the game.
 */
public class EventResolver {
    /**
     * The game instance used for resolving event effects.
     */
    private final Game game;

    /**
     * Constructs an EventResolver with the specified game instance.
     *
     * @param game the game instance to be used for resolving event effects.
     */
    public EventResolver(Game game) {
        this.game = game;
    }

    /**
     * Resolves the effects of the given list of event cards according to the game rules.
     *
     * @param eventCards a list of EventCard objects representing the event cards to be resolved.
     */
    public void resolveEvents(List<EventCard> eventCards) {
        // Sort the event cards based on the game rules
        Stack<EventCard> sortedEventCards = game.getRuleSet().sortEventCards(eventCards);

        // Apply each event card in the sorted order
        while (!sortedEventCards.isEmpty()) {
            EventCard eventCard = sortedEventCards.pop();
            EventType eventType = eventCard.getEventType();

            // Generate the base effect of the event card for all players
            List<EffectEventResult> changes = eventCard.getEventEffect().calculateBaseEffect(game, eventCard);

            // Apply any active modifiers from the players' decks to the base effect results
            for (EffectEventResult change : changes) {
                Player p = change.getTargetPlayer();

                List<IEventModifier> activeModifiers = p.getPlayerDeck().getActiveEventModifiers(eventType);
                for (IEventModifier modifier : activeModifiers) {
                    modifier.applyModifier(eventType, change);
                }

                // Refine the change based on specific rules for certain event types, such as the Sustenance event
                refineChange(change, eventCard);
            }

            // Finally, apply all the changes to the players' resources based on the log of effect results
            applyAllChanges(changes);
        }
    }

    private void refineChange(EffectEventResult change, EventCard eventCard) {
        EventType eventType = eventCard.getEventType();
        Player player = change.getTargetPlayer();

        if (eventType == EventType.SUSTENANCE && change.getFoodChange() < 0) {

            // Calculate the total food deficit after applying the change
            int foodDeficit = player.getFood() + change.getFoodChange();

            if (foodDeficit < 0) {
                // If the player cannot pay the food cost, he pays all his remaining food...
                change.setFoodChange(-player.getFood());

                // ...and loses PP for each unfed Character in the tribe, based on the malus indicated on the Event card
                int ppLoss = Math.abs(foodDeficit) * eventCard.getMalusPP();
                change.setPpChange(change.getPpChange() - ppLoss);
            }
        }
    }

    /**
     * Applies all the changes to the players based on the log of effect results.
     *
     * @param allChangesLog a list of EffectEventResult objects representing the changes to be applied to the players.
     */
    private void applyAllChanges(List<EffectEventResult> allChangesLog) {
        for (EffectEventResult change : allChangesLog) {
            change.getTargetPlayer().addFood(change.getFoodChange());
            change.getTargetPlayer().addPP(change.getPpChange());
        }
    }
}
