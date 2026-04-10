package it.polimi.ingsw.model.effect.building.endgame;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;


public class EndPpCardsSetBuildingEffect implements IBuildingEffect, IEndGameScoring {

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
}
