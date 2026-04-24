package it.polimi.ingsw.server.model.effect.building.endgame;

import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;

import java.util.Optional;

/**
 * At the end of the game, earn the Prestige Points indicated for each Character card of the type indicated.
 */
public class PpPerCharacterBuildingEffect implements IBuildingEffect, IEndGameScoring {

    /**
     * The amount of PP gained by the player for each
     */
    private final int PPGained;

    /**
     * The type of character that this effect is related to
     */
    private final CharacterType characterType;

    /**
     * Constructs a new PpPerCharacterBuildingEffect with the specified amount of PP gained and character type.
     *
     * @param PP            the amount of PP gained by the player for each character card of the specified type
     * @param characterType the type of character that this effect is related to
     */
    public PpPerCharacterBuildingEffect(int PP, CharacterType characterType) {
        this.PPGained = PP;
        this.characterType = characterType;
    }


    /**
     * Calculate the end game bonus provided by this end game scoring effect for the given player.
     *
     * @param player the player for which the end game scoring effect is being calculated
     * @return The prestige points extra that the player will receive at the end of the game thanks to this end game scoring effect
     */
    @Override
    public int calculateEndGameBonus(Player player) {
        return PPGained * player.getPlayerDeck().countCharactersByType(characterType);
    }

    @Override
    public Optional<IEndGameScoring> getEndGameScoring(){
        return Optional.of(this);
    }
}
