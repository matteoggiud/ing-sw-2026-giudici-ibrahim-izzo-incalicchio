package it.polimi.ingsw.model.effect;

import it.polimi.ingsw.model.core.Player;

public class EffectEventResult {
    private final Player targetPlayer;
    private final int foodChange;
    private final int ppChange;

    public EffectEventResult(int foodChange, int ppChange,  Player targetPlayer) {
        this.foodChange = foodChange;
        this.ppChange = ppChange;
        this.targetPlayer = targetPlayer;
    }

    public int getFoodChange() {
        return foodChange;
    }

    public int getPpChange() {
        return ppChange;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

}
