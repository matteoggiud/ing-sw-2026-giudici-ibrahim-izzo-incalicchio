package it.polimi.ingsw.model.card;

/**
 * This class represents a tribe card, which is a type of card in the game.
 */
public class TribeCard extends Card{

    /**
     * Construct a new TribeCard with specific properties.
     * @param name The name of the card
     * @param era The era of the card
     * @param minNumPlayers The minimum number of player to use this card
     */
    public TribeCard(String name, Era era, int minNumPlayers) {
        super(name, era, minNumPlayers);
    }
}
