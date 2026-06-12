package it.polimi.ingsw.server.model.effect.building.extraction;

/**
 * This interface represents a building effect that grants an extra action to the player.
 * Implementing classes will define how to determine if the effect provides an extra action.
 */
public interface IExtraActionEffect {

    /**
     * Checks if the effect has an extra action.
     *
     * @return true if the effect has an extra action, false otherwise.
     */
    boolean hasExtraAction();
}
