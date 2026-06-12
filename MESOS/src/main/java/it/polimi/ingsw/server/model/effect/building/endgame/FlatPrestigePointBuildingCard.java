package it.polimi.ingsw.server.model.effect.building.endgame;


import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;

import java.util.Optional;

/**
 * This class represents the effect of a building card that grants
 * a flat amount of prestige points at the end of the game.
 */
public class FlatPrestigePointBuildingCard implements IBuildingEffect, IEndGameScoring {

    /**
     * Calculate the end game bonus provided by this end game scoring effect for the given player.
     *
     * @param player the player for which the end game scoring effect is being calculated
     * @return The prestige points extra that the player will receive at the end of the game thanks to this end game scoring effect
     */
    @Override
    public int calculateEndGameBonus(Player player) {
        return 25;
    }

    /**
     * Indicates that this effect provides endgame scoring.
     * @return an Optional containing this instance as an IEndGameScoring effect
     */
    @Override
    public Optional<IEndGameScoring> getEndGameScoring(){
        return Optional.of(this);
    }

}
