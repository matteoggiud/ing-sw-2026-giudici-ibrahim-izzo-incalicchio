package it.polimi.ingsw.server.model.card.characters;

import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;

/**
 * Represents a Hunter Character Card in the game.
 */
public class HunterCharacterCard extends CharacterCard {
    /**
     * Indicates whether this Hunter card has a food icon.
     */
    private final boolean isFoodIcon;

    /**
     * Constructs a new HunterCharacterCard with the specified attributes.
     *
     * @param cardId        the unique identifier of the card
     * @param name          the name of the card
     * @param era           the era of the card (Era I, II, III)
     * @param minNumPlayers the minimum number of players required for this card to be in the game (>= 2 && <= 5)
     * @param isFoodIcon    indicates whether this Hunter card has a food icon (true if it does, false otherwise)
     * @param imageKey      the key used to retrieve the card's image from the resource manager
     * @throws IllegalArgumentException if numeric constraints are violated or if something is null
     */
    public HunterCharacterCard(int cardId, String name, Era era, int minNumPlayers, boolean isFoodIcon, String imageKey) throws IllegalArgumentException {
        super(cardId, name, era, minNumPlayers, imageKey);
        this.isFoodIcon = isFoodIcon;
    }

    /**
     * Checks if this Hunter card has a food icon.
     *
     * @return true if this Hunter card has a food icon, false otherwise.
     */
    @Override
    public boolean hasFoodIcon() {
        return isFoodIcon;
    }

    /**
     * When this Hunter card is added to a player, if it has a food icon,
     * grant food bonus equal to the total number of hunters in the player's deck.
     *
     * @param player the player to whom this card is being added
     */
    @Override
    public void onAddedToPlayer(Player player) {
        //TODO: void to int
        if (isFoodIcon) {
            int hunterCount = player.getPlayerDeck().countCharactersByType(CharacterType.HUNTER);
            player.addFood(hunterCount);
        }
    }

    /**
     * @param playerDeck the player's deck to which this card is being added.
     */
    @Override
    public void onAddToPlayerDeck(PlayerDeck playerDeck) {
        playerDeck.incrementTypeCount(CharacterType.HUNTER);

    }

}
