package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.core.Player;

public interface IEndGameScoring {
    /**
     * Calculate the end game bonus provided by this end game scoring effect for the given player.
     *
     * @param player the player for which the end game scoring effect is being calculated
     * @return The prestige points extra that the player will receive at the end of the game thanks to this end game scoring effect
     */
    int calculateEndGameBonus(Player player);
}
