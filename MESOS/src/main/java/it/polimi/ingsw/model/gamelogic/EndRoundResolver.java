package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.extraction.IExtraActionEffect;
import it.polimi.ingsw.model.effect.building.triggers.IEventTrigger;

import java.util.ArrayList;
import java.util.List;

public class EndRoundResolver {
    /**
     * The game instance used for resolving event effects.
     */
    private final Game game;

    /**
     * Constructs an EventResolver with the specified game instance.
     *
     * @param game the game instance to be used for resolving event effects.
     */
    public EndRoundResolver(Game game) {
        this.game = game;
    }

    /**
     * Resolves the effects of active triggers from the player's deck at the end of the turn.
     *
     * @param player
     */
    public void resolveExtraction(Player player) {

        //Cycle to apply eventual character and building effect given be the event
        List<IExtraActionEffect> activeBuildingExtraction = player.getPlayerDeck().getActiveExtractionEffects();
        for (IExtraActionEffect buildingExtraction : activeBuildingExtraction) {
            //DRAW A CARD (CONTROLLER);
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
