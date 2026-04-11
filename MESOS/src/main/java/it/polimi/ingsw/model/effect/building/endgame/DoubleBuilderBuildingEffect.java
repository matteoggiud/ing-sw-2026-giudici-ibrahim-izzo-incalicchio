package it.polimi.ingsw.model.effect.building.endgame;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.model.core.Player;

import java.util.Optional;

/**
 * At the end of the match you get doubled the PP of all the Builders
 * In N_Subject goes all the PP given by the Builders
 */
public class DoubleBuilderBuildingEffect implements IBuildingEffect, IEndGameScoring {

    @Override
    public int calculateEndGameBonus(Player player) {
        int n_builders=0;
        for(CharacterCard c : player.getPlayerDeck().getCharacters()){
            if(c.getType().equals(CharacterType.BUILDER)){
                n_builders++;
            }
        }
        return 2 * n_builders;
    }

    @Override
    public Optional<IEndGameScoring> getEndGameScoring(){
        return Optional.of(this);
    }
}
