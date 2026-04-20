package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.deck.PlayerDeck;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.model.board.CardRow;
import it.polimi.ingsw.model.core.Player;


/**
 * Represents a Building Card in the game.
 * Building cards provide end-game prestige points and special effects,
 * and require a specific food cost to be built.
 */
public class BuildingCard extends TribeCard {

    /** The amount of food required to build this card. */
    private final int foodCost;

    /** The number of prestige points awarded at the end of the game. */
    private final int endGamePP;

    /** The specific effect triggered by this building. */
    private final IBuildingEffect effect;

    /**
     * Constructs a new BuildingCard.
     *
     * @param name the name of the building card
     * @param era the era of the building card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @param foodCost the amount of food required to build it (>= 3 && <= 9)
     * @param endGamePP the prestige points awarded at the end of the game (>= 2)
     * @param effect the effect associated with the building (must not be null)
     * @throws IllegalArgumentException if numeric constraints are violated or if effect is null
     */
    public BuildingCard(String name, Era era, int minNumPlayers, int foodCost, int endGamePP, IBuildingEffect effect) throws IllegalArgumentException {
        super(name, era, minNumPlayers);

        if (foodCost < 3 || foodCost > 9) {
            throw new IllegalArgumentException("Food cost must be between 3 and 9.");
        }
        if (endGamePP < 2) {
            throw new IllegalArgumentException("End game prestige points must be at least 2.");
        }
        if (effect == null) {
            throw new IllegalArgumentException("Effect cannot be null.");
        }

        this.foodCost = foodCost;
        this.endGamePP = endGamePP;
        this.effect = effect;
    }

    /**
     * Gets the food cost required to build this card.
     *
     * @return the food cost.
     */
    public int getFoodCost() {
        return foodCost;
    }

    /**
     * Gets the end game prestige points awarded by this building card.
     *
     * @return the end game prestige points.
     */
    public int getEndGamePP() {
        return endGamePP;
    }

    /**
     * Gets the effect associated with this building card.
     *
     * @return the building effect.
     */
    public IBuildingEffect getEffect(){
        return effect;
    }

    /**
     * Adds this card to the specified player's deck.
     *
     * @param playerDeck the player's deck to which this card will be added
     */
    @Override
    public void addTo(PlayerDeck playerDeck) {
        playerDeck.addBuildingCard(this);
    }

    @Override
    public void validateSelectableFrom(CardRow row) {
        if (!row.getBuildingRow().getBuildingCards().contains(this)) {
            throw new IllegalArgumentException("Selected building is not available in the building row.");
        }
    }

    @Override
    public void resolveFromOfferRow(Player player, CardRow row) {
        player.canBuyBuildingCard(this);
        row.getBuildingRow().remove(this);
    }


}
