package it.polimi.ingsw.server.model.effect.building.triggers;

import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.characters.InventorType;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TriggerInventorsCoupleBuildingEffect implements IBuildingEffect, IEventTrigger {
    /**
     * The amount of food to pay for each set of characters completed after buying the building.
     */
    private final static int FOOD_TO_PAY = 3;

    /**
     * The number of complete sets of characters the player had when they bought the building.
     */
    private int InventorCoupleAlreadyCompletedWhenBought;

    /**
     * The number of sets of characters that have been completed since the building was bought.
     */
    private int payedCouples = 0;

    @Override
    public Optional<IEventTrigger> getEventTrigger() {
        return Optional.of(this);
    }

    /**
     * @param player the player who bought the building
     */
    @Override
    public void applyImmediateEffect(Player player) {
        InventorCoupleAlreadyCompletedWhenBought = calculateCurrentCouples(player);
    }

    /**
     * @param player the player for which to check the trigger condition
     * @param game the current game state
     */
    @Override
    public int getTrigger(Player player, Game game) {
        int currentCouples = calculateCurrentCouples(player);

        if (currentCouples > InventorCoupleAlreadyCompletedWhenBought + payedCouples) {
            // We have to pay

            payedCouples++;
            return FOOD_TO_PAY;
        }
        return 0;
    }

    /**
     * Calculate the number of complete couples of inventors that the player currently has.
     *
     * @param player the player for which to calculate the number of complete couples of inventors
     * @return the number of complete couples of inventors that the player currently has
     */
    private int calculateCurrentCouples(Player player) {
        Map<InventorType, Integer> frequency = new HashMap<>();
        int n_couple = 0;

        List<CharacterCard> inventors = player.getPlayerDeck().getCharacters();

        for (CharacterCard inventor : inventors) {
            inventor.getInventorType().ifPresent(type -> frequency.put(type, frequency.getOrDefault(type, 0) + 1));
        }

        for (Integer count : frequency.values()) {
            n_couple += count / 2;
        }
        return n_couple;
    }

}
