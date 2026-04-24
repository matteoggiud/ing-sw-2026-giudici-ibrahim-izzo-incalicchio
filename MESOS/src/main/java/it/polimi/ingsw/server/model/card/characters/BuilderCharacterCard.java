package it.polimi.ingsw.server.model.card.characters;

import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.card.Era;
import it.polimi.ingsw.server.model.deck.PlayerDeck;

/**
 * Represents a Builder Character Card in the game.
 * Builder cards provide an end-game PP bonus and a food discount during the game.
 */
public class BuilderCharacterCard extends CharacterCard {
    private final int endGamePP;
    private final int foodDiscount;

    /**
     * Constructs a new BuilderCharacterCard with the specified attributes.
     *
     * @param name the name of the card
     * @param era the era of the card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @param endGamePP the number of victory points this card provides at the end of the game (must be >= 0)
     * @param foodDiscount the amount of food discount this card provides during the game (must be >= 0)
     */
    public BuilderCharacterCard(int cardId, String name, Era era, int minNumPlayers, int endGamePP, int foodDiscount) throws IllegalArgumentException {
        super(cardId, name, era, minNumPlayers);

        if (endGamePP < 0)
            throw new IllegalArgumentException("End Game PP must not be negative");

        if (foodDiscount < 0)
            throw new IllegalArgumentException("Food Discount must not be negative");

        this.endGamePP = endGamePP;
        this.foodDiscount = foodDiscount;
    }


    /**
     * Gets the number of prestige points this Builder card provides at the end of the game.
     *
     * @return the end-game prestige points provided by this card.
     */
    @Override
    public int getEndGamePPBonus() {
        return endGamePP;
    }

    /**
     * @param playerDeck the player's deck to which this card is being added.
     */
    @Override
    public void onAddToPlayerDeck(PlayerDeck playerDeck) {
        playerDeck.incrementTypeCount(CharacterType.BUILDER);
    }

    /**
     * Gets the amount of food discount this Builder card provides during the game.
     *
     * @return the food discount provided by this card during the game.
     */
    @Override
    public int getBuilderFoodDiscount() {
        return foodDiscount;
    }

}
