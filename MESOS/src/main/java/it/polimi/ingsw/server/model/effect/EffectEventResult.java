package it.polimi.ingsw.server.model.effect;

import it.polimi.ingsw.server.model.core.Player;

/**
 * This class represents the result of applying an effect in the game, specifically for events.
 */
public class EffectEventResult {
    private final Player targetPlayer;
    private int foodChange;
    private int ppChange;

    /**
     * Constructor for the EffectEventResult class.
     * @param foodChange the change in the player's food resources as a result of applying the effect.
     * @param ppChange the change in the player's PP score as a result of applying the effect.
     * @param targetPlayer the player who is the target of the effect being applied.
     */
    public EffectEventResult(int foodChange, int ppChange,  Player targetPlayer) {
        this.foodChange = foodChange;
        this.ppChange = ppChange;
        this.targetPlayer = targetPlayer;
    }

    /**
     * Gets the change in the player's food resources as a result of applying the effect.
     * @return the change in the player's food resources as a result of applying the effect.
     */
    public int getFoodChange() {
        return foodChange;
    }

    /**
     * Gets the change in the player's PP score as a result of applying the effect.
     * @return the change in the player's PP score as a result of applying the effect.
     */
    public int getPpChange() {
        return ppChange;
    }

    /**
     * Gets the player who is the target of the effect being applied.
     * @return the player who is the target of the effect being applied.
     */
    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public void setFoodChange(int foodChange) { this.foodChange = foodChange; }

    public void setPpChange(int ppChange) { this.ppChange = ppChange; }

}
