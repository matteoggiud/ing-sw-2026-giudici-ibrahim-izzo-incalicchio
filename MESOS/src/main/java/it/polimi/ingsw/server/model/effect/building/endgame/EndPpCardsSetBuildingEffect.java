package it.polimi.ingsw.server.model.effect.building.endgame;

import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;

import java.util.Optional;

/**
 * This class represents the effect of a building that grants end-game points
 * based on the number of complete sets of character cards a player has.
 */
public class EndPpCardsSetBuildingEffect implements IBuildingEffect, IEndGameScoring {

    /**
     * Calculates the end game bonus for a player
     * based on the number of complete sets of character cards they have.
     * @param player the player for which the end game scoring effect is being calculated
     * @return the end game bonus points for the player
     */
    @Override
    public int calculateEndGameBonus(Player player) {
        int n_set_min = Integer.MAX_VALUE;
        for(CharacterType type : CharacterType.values()){
            int countCharacterType = player.getPlayerDeck().countCharactersByType(type);
            if(countCharacterType < n_set_min){
                n_set_min = countCharacterType;
            }
        }
        return n_set_min * 6;
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
