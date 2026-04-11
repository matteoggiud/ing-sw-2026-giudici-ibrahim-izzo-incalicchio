package it.polimi.ingsw.model.effect.building.triggers;

import it.polimi.ingsw.model.board.TurnOrderSlot;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;

import java.util.Optional;

/**
 * At the end of the turn if your totem goes on a spot with a Food Bonus,
 * you get an additional Food otherwise the effect doesn't apply
 */

public class TotemBonusBuildingEffect implements IBuildingEffect, IEventTrigger {
    /**
     * The amount of food gained by this building effect if the player isn't in the last position of the turn order.
     */

    private final static int FOOD_TO_PAY = 1;

    /**
     * If the building implements IEventTrigger it returns an Optional containing himself.
     *
     * @return an Optional containing the IEventTrigger provided by this building effect.
     */
    @Override
    public Optional<IEventTrigger> getEventTrigger() {
        return IBuildingEffect.super.getEventTrigger();
    }

    /**
     * This method is called to evaluate the trigger condition and calculate the amount of food gained by this building effect.
     *
     * @param player the player for which the trigger is being evaluated
     * @param game   the current game state
     * @return the amount of food gained by this building effect
     */
    @Override
    public int getTrigger(Player player, Game game) {
        TurnOrderSlot turnOrderSlot = game.getBoard().getTurnOrderBox().getSlotByPlayer(player);

        if (game.getBoard().getTurnOrderBox().getSlotByPlayer(player).getFoodBonus() > 0) {
            // The player is in a position of the turn order that grants a Food Bonus.
            return FOOD_TO_PAY;
        }

        return 0;
    }
}
