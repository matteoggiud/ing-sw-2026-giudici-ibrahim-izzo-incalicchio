package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.card.characters.InventorType;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.board.CardRow;


import java.util.List;
import java.util.Optional;

/**
 * This class represents a character card, which is a type of card in the game.
 */
public abstract class CharacterCard extends TribeCard {

    /**
     * Construct a new CharacterCard with specific properties.
     * @param name the name of the character card
     * @param era the era to which the character card belongs
     * @param minNumPlayers the minimum number of players required to use this character card
     * @param imageKey the key used to retrieve the card's image from the resource manager
     * @throws IllegalArgumentException if numeric constraints are violated or something is null.
     */
    public CharacterCard(int cardId, String name, Era era, int minNumPlayers, String imageKey) throws IllegalArgumentException {
        super(cardId ,name, era, minNumPlayers, imageKey);
    }

    public int getBuilderFoodDiscount() { return 0; }
    public int getShamanStars() { return 0; }
    public Optional<InventorType> getInventorType() { return Optional.empty(); }
    public int getEndGamePPBonus() { return 0; }
    public boolean hasFoodIcon() { return false; }



    /**
     * Hook called when this character card is added to a player's deck.
     * Subclasses can override this to apply special effects upon resolution.
     *
     * @param player the player to whom this card is being added
     */
    public void onAddedToPlayer(Player player) {
        // Default: no special effect
    }

    /**
     * Adds this card to the specified player's deck.
     *
     * @param playerDeck the player's deck to which this card will be added
     */
    @Override
    public void addTo(PlayerDeck playerDeck) {
        playerDeck.addCharacterCard(this);
    }

    @Override
    public void addToCharacterList(List<CharacterCard> characterList) {
        characterList.add(this);
    }

    @Override
    public boolean canBeSelectedFrom(CardRow row, Player player) {
        return row.getTribeRow().getCharacterCards().contains(this);
    }

    @Override
    public void validateSelectableFrom(CardRow row) {
        if (!row.getTribeRow().getCharacterCards().contains(this)) {
            throw new IllegalArgumentException("Selected character is not available in the character row.");
        }
    }

    @Override
    public void resolveFromOfferRow(Player player, CardRow row) {
        row.getTribeRow().remove(this);
        player.getPlayerDeck().addCard(this);
        onAddedToPlayer(player);
    }

    /**
     * Hook method that can be overridden by subclasses to apply special effects when this character card is added to a player's deck.
     *
     * @param playerDeck the player's deck to which this card is being added.
     */
    public abstract void onAddToPlayerDeck(PlayerDeck playerDeck);

}
