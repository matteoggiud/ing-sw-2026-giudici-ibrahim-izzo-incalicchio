package it.polimi.ingsw.model.card;

import java.util.List;

/**
 * This class represents a tribe card, which is a type of card in the game.
 */
public abstract class TribeCard extends Card{




    /**
     * Construct a new TribeCard with specific properties.
     *
     * @param cardId The unique identifier of the card
     * @param name The name of the card
     * @param era The era of the card
     * @param minNumPlayers The minimum number of player to use this card
     */
    protected TribeCard(int cardId, String name, Era era, int minNumPlayers) {
        super(cardId, name, era, minNumPlayers);
    }

    public boolean isFinalEvent(){
        return false;
    }

    public boolean goesInUpperRow(){
        return false;
    }

    public void addToCharacterList(List<CharacterCard> characterList){
        // default: do nothing
    }

    public void addToEventList(List<EventCard> eventList){
        // default: do nothing
    }


}
