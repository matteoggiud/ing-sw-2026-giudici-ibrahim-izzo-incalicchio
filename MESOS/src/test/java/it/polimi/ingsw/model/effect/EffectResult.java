package it.polimi.ingsw.model.effect;

/**
 * This class represents the result of applying an effect in the game.
 */
public class EffectResult {

    /**
     * The changes in the player's resources and points as a result of applying the effect.
     */
    private int foodDelta;

    /**
     * The change in the player's PP score as a result of applying the effect.
     */
    private int ppDelta;

    /**
     * The change in the player's star score as a result of applying the effect.
     */
    private int starDelta;

    /**
     * The change in the player's card count as a result of applying the effect.
     */
    private int cardDelta;

    /**
     * Constructor for the EffectResult class.
     * @param foodDelta Food difference
     * @param ppDelta PP difference
     * @param starDelta Star difference
     * @param cardDelta Additional card to pick
     */
    public EffectResult(int foodDelta, int ppDelta, int starDelta, int cardDelta) {
        this.foodDelta = foodDelta;
        this.ppDelta = ppDelta;
        this.starDelta = starDelta;
        this.cardDelta = cardDelta;
    }

    /**
     * Gets the FoodDelta
     * @return FoodDelta
     */
    public int getFoodDelta() {
        return foodDelta;
    }

    /**
     * Gets the PPDelta
     * @return PPDelta
     */
    public int getPpDelta() {
        return ppDelta;
    }

    /**
     * Gets the StarDelta
     * @return StarDelta
     */
    public int getStarDelta() {
        return starDelta;
    }

    /**
     * Gets the CardDelta
     * @return CardDelta
     */
    public int getCardDelta() {
        return cardDelta;
    }
}

