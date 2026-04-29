package it.polimi.ingsw.server.model.gamelogic;

import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.modifiers.IEventModifier;
import it.polimi.ingsw.server.model.effect.building.triggers.IEventTrigger;
import it.polimi.ingsw.server.model.observer.dto.EventCardDTO;
import it.polimi.ingsw.server.model.observer.dto.mapper.EventCardDTOMapper;
import it.polimi.ingsw.server.network.socket.messages.game.ResolvingEventUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The EventResolver class is responsible for resolving the effects of event cards in the game.
 */
public class EffectResolver {
    /**
     * The game instance used for resolving event effects.
     */
    private final Game game;

    /**
     * Constructs an EventResolver with the specified game instance.
     *
     * @param game the game instance to be used for resolving event effects.
     */
    public EffectResolver(Game game) {
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

            game.getObserverManager().notifyObservers(new ResolvingEventUpdate(game.getGameId(), EventCardDTOMapper.toEventCardDTO(eventCard)));

            // Generate the base effect of the event card for all players
            List<EffectEventResult> changes = eventCard.getEventEffect().calculateBaseEffect(game, eventCard);

            // Apply any active modifiers from the players' decks to the base effect results
            for (EffectEventResult change : changes) {
                Player p = change.getTargetPlayer();

                //Cycle to apply eventual character and building effect given be the event
                List<IEventModifier> activeBuildingModifiers = p.getPlayerDeck().getActiveBuildingEventModifiers();
                for (IEventModifier buildingModifier : activeBuildingModifiers) {
                    eventCard.getEventEffect().acceptModifier(buildingModifier, change);
                }

                // Allow the event effect to post-process the change (e.g., Sustenance handling food deficits)
                eventCard.getEventEffect().postProcessChange(change, eventCard);
            }


            // Finally, apply all the changes to the players' resources based on the log of effect results
            applyAllChanges(changes);
        }
    }

    /**
     * Resolves the effects of active triggers from the player's deck at the end of the turn.
     *
     * @param player the player for whom the triggers are to be resolved.
     */
    public void resolveTrigger(Player player) {
                List<EffectEventResult> changes = new ArrayList<>();
                //Cycle to apply eventual character and building effect given be the event
                List<IEventTrigger> activeBuildingTriggers = player.getPlayerDeck().getActiveBuildingEventTriggers();
                for (IEventTrigger buildingModifier : activeBuildingTriggers) {
                    changes.add(new EffectEventResult(buildingModifier.getTrigger(player, game), 0, player));
                }
                // Finally, apply all the changes to the players' resources based on the log of effect results
                applyAllChanges(changes);
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
