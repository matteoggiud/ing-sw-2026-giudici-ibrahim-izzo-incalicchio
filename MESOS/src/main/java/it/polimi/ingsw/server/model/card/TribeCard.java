package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.era.Era;

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
     * @param imageKey The key used to retrieve the card's image from the resource manager
     */
    protected TribeCard(int cardId, String name, Era era, int minNumPlayers, String imageKey) {
        super(cardId, name, era, minNumPlayers, imageKey);
    }

    /**
     * Indicates whether this card is a final event card, which should be placed at the end of the deck.
     * By default, this returns false, but it can be overridden by subclasses (like EventCard)
     * to indicate that they are final events.
     * @return true if this card is a final event, false otherwise
     */
    public boolean isFinalEvent(){
        return false;
    }

    /**
     * Indicates whether this card should be placed in the upper row of the board.
     * By default, this returns false.
     * @return
     */
    public boolean goesInUpperRow(){
        return false;
    }

    /**
     * Adds this card to the provided character list.
     * This is used when initializing the character row on the board.
     * @param characterList the list to which this character card will be added
     */
    public void addToCharacterList(List<CharacterCard> characterList){
        // default: do nothing
    }

    /**
     * Adds this card to the provided event list.
     * @param eventList the list to which this event card will be added
     */
    public void addToEventList(List<EventCard> eventList){
        // default: do nothing
    }

    /**
     * Sorts this card into the appropriate era list based on its era. This is used when initializing the card rows on the board.
     * @param eraI
     * @param eraIi
     * @param eraIii
     * @param finalEvents
     */
    public void selfSort(List<TribeCard> eraI, List<TribeCard> eraIi, List<TribeCard> eraIii, List<TribeCard> finalEvents) {
        getEra().selfSortByEra(this, eraI, eraIi, eraIii, finalEvents);
    }
}
