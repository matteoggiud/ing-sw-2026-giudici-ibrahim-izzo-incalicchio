package it.polimi.ingsw.model.effect.building.triggers;


import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;


public interface IEventTrigger {

    default boolean isCardTrigger() {
        return false;
    }

    /**
     * This method is called when the building with this effect is bought, to apply any immediate effect that it may have.
     *
     * @param player the player who bought the building
     * @throws UnsupportedOperationException if the building effect does not have an immediate effect
     */
    default void applyImmediateEffect(Player player) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This building effect does not have an immediate effect");
    }

    /**
     * This method is called to evaluate the trigger condition and calculate the amount of food gained by this building effect.
     *
     * @param player the player for which the trigger is being evaluated
     * @param game   the current game state
     * @return the amount of food gained by this building effect
     *
     */
    int getTrigger(Player player, Game game); //TODO: Instead of returning an int we return an EffectEventResult so it is more scalable in the future and more consistent with the rest of the code

    //TODO: Put trigger type or understand if we can properly manage the order to call the triggers so that there is no risk that the triggers will not be called at the correct time. Or we can use the game states to understand if the triggers have an effect in the correct state
}
