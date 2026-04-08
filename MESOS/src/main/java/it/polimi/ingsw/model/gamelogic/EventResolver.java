package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.effect.EffectEventResult;

import java.util.ArrayList;
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
    public void resolveEvent(List<EventCard> eventCards) {
        // Sort the event cards based on the game rules
        Stack<EventCard> sortedEventCards = game.getRuleSet().sortEventCards(eventCards);

        // Apply each event card in the sorted order
        while (!sortedEventCards.isEmpty()) {
            EventCard eventCard = sortedEventCards.pop();
            List<EffectEventResult> changes = eventCard.getEventEffect().ApplyEffect(game, eventCard);

            applyAllChanges(changes);
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
