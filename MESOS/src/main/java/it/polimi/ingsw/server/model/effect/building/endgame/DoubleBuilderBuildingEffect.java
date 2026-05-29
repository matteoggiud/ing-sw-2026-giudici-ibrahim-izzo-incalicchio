package it.polimi.ingsw.server.model.effect.building.endgame;

import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.core.Player;

import java.util.Optional;

/**
 * At the end of the match you get doubled the PP of all the Builders
 * In N_Subject goes all the PP given by the Builders
 */
public class DoubleBuilderBuildingEffect implements IBuildingEffect, IEndGameScoring {

    @Override
    public int calculateEndGameBonus(Player player) {
        return player.getPlayerDeck().getCharacters().stream()
                .filter(card -> player.getPlayerDeck().countCharactersByType(CharacterType.BUILDER) > 0)
                .mapToInt(card -> card.getBuilderFoodDiscount() >= 0 && card.getEndGamePPBonus() > 0
                        ? card.getEndGamePPBonus()
                        : 0)
                .sum();
    }

    @Override
    public Optional<IEndGameScoring> getEndGameScoring() {
        return Optional.of(this);
    }
}
