package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.characters.InventorType;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.deck.PlayerDeck;
import it.polimi.ingsw.model.board.CardRow;


import java.util.List;
import java.util.Optional;

/**
 * This class represents a character card, which is a type of card in the game.
 */
public class CharacterCard extends TribeCard {

    private final CharacterType type;

    /**
     * Construct a new CharacterCard with specific properties.
     * @param name the name of the character card
     * @param era the era to which the character card belongs
     * @param minNumPlayers the minimum number of players required to use this character card
     * @throws IllegalArgumentException if numeric constraints are violated or something is null.
     */
    public CharacterCard(String name, Era era, int minNumPlayers, CharacterType type) throws IllegalArgumentException {
        super(name, era, minNumPlayers);
        this.type = type;
    }

    public int getBuilderFoodDiscount() { return 0; }
    public int getShamanStars() { return 0; }
    public Optional<InventorType> getInventorType() { return Optional.empty(); }
    public int getEndGamePPBonus() { return 0; }
    public boolean hasFoodIcon() { return false; }

    /**
     * Gets the type of this character card (e.g., HUNTER, GATHERER, SHAMAN, INVENTOR).
     *
     * @return the type of this character card.
     */
    public CharacterType getType() { return type; }

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
    public void validateSelectableFrom(CardRow row, String rowName) {
        if (!row.getTribeRow().getCharacterCards().contains(this)) {
            throw new IllegalArgumentException("Selected character is not available in the " + rowName + " row.");
        }
    }

    @Override
    public void resolveFromOfferRow(Player player, CardRow row) {
        row.getTribeRow().remove(this);
        player.getPlayerDeck().addCard(this);
        onAddedToPlayer(player);
    }

}
