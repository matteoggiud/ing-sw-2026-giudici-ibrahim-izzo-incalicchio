package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.effect.EffectResult;

/**
 * This interface represents the effect of a building card in the game.
 * Each building card has a specific effect that can be applied during the game,
 * and this interface defines the method to apply that effect.
 */
public interface IBuildingEffect {
    EffectResult ApplyEffect(int N_Subject);
}
