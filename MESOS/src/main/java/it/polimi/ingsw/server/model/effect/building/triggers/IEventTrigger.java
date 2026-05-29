package it.polimi.ingsw.server.model.effect.building.triggers;


import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;


public interface IEventTrigger {

    /**
     * This method is called when the building with this effect is bought, to apply any immediate effect that it may have.
     *
     * @param player the player who bought the building
     * @throws UnsupportedOperationException if the building effect does not have an immediate effect
     */
    default void applyImmediateEffect(Player player) throws UnsupportedOperationException {
        // By default, there is no immediate effect
    }

    /**
     * This method is called to evaluate the trigger condition and calculate the amount of food gained by this building effect.
     *
     * @param player the player for which the trigger is being evaluated
     * @param game   the current game state
     * @return the amount of food gained by this building effect
     *
     */
    int getTrigger(Player player, Game game);

}
