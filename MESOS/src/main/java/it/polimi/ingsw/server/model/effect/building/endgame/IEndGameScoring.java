package it.polimi.ingsw.server.model.effect.building.endgame;

import it.polimi.ingsw.server.model.core.Player;

/**
 * This interface represents an endgame scoring effect provided by a building card.
 * Implementing classes will define how to calculate the endgame bonus
 * for a player based on the specific effect of the building card.
 */
public interface IEndGameScoring {
    /**
     * Calculate the end game bonus provided by this end game scoring effect for the given player.
     *
     * @param player the player for which the end game scoring effect is being calculated
     * @return The prestige points extra that the player will receive at the end of the game thanks to this end game scoring effect
     */
    int calculateEndGameBonus(Player player);
}
