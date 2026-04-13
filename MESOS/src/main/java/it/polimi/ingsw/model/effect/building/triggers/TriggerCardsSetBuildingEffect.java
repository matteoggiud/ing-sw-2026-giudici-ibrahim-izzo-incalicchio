package it.polimi.ingsw.model.effect.building.triggers;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;

import java.util.Optional;

/**
 * For every character set you get a Food bonus
 */
public class TriggerCardsSetBuildingEffect implements IBuildingEffect, IEventTrigger {
    /**
     * The amount of food to pay for each set of characters completed after buying the building.
     */
    private final static int FOOD_TO_PAY = 5;

    /**
     * The number of complete sets of characters the player had when they bought the building.
     */
    private int setsAlreadyCompletedWhenBought;

    /**
     * The number of sets of characters that have been completed since the building was bought.
     */
    private int payedSets = 0;

    @Override
    public Optional<IEventTrigger> getEventTrigger() {
        return Optional.of(this);
    }

    /**
     * @param player the player who bought the building
     */
    @Override
    public void applyImmediateEffect(Player player) {
        setsAlreadyCompletedWhenBought = calculateCurrentSets(player);
    }

    /**
     * @param player the player for which to check the trigger condition
     * @param game the current game state
     * @return the amount of food gained
     */
    @Override
    public int getTrigger(Player player, Game game) {
        int currentSets = calculateCurrentSets(player);

        if (currentSets > setsAlreadyCompletedWhenBought + payedSets) {
            // We have to pay
            payedSets++;
            return FOOD_TO_PAY;
        }
        return 0;
    }

    /**
     * Calculate the number of complete sets of characters that the player currently has.
     *
     * @param player the player for which to calculate the number of complete sets of characters
     * @return the number of complete sets of characters that the player currently has
     */
    private int calculateCurrentSets(Player player) {
        int n_set_min = Integer.MAX_VALUE;
        for (CharacterType type : CharacterType.values()) {
            int countCharacterType = player.getPlayerDeck().countCharactersByType(type);
            if (countCharacterType < n_set_min) {
                n_set_min = countCharacterType;
            }
        }
        return n_set_min;
    }


    @Override
    public boolean isCardTrigger() {
        return true;
    }
}
