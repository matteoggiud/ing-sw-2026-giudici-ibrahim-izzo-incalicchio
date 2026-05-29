package it.polimi.ingsw.server.model.era;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.deck.BuildingDeck;

import java.util.HashMap;
import java.util.List;

/**
 * Enumeration representing the three eras in the game: Era I, Era II, and Era III.
 */
public abstract class Era {
    /**
     * Sorts the given tribe card into the appropriate era list based on its era and type.
     * @param tribeCard the tribe card to be sorted into the correct era list.
     * @param eraI the list of tribe cards belonging to Era I.
     * @param eraII the list of tribe cards belonging to Era II.
     * @param eraIII the list of tribe cards belonging to Era III.
     * @param finalEvents the list of event cards that are added to the final events list, if applicable.
     */
    public abstract void selfSortByEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents);

    /**
     * Creates a building deck for the era by adding
     * the appropriate building cards to the given building decks map.
     * @param buildingDecks a map of eras to their corresponding building decks,
     * which will be modified by adding the building cards for this era.
     * @return the updated map of eras to building decks after adding the building cards for this era
     */
    public abstract HashMap<Era, BuildingDeck> createBuildingDeck(HashMap<Era, BuildingDeck> buildingDecks);

    /**
     * Returns the order of the era for sorting purposes,
     * with Era I having the lowest order and Era III having the highest order.
     * @return the order of the era.
     */
    public abstract int getOrder();

    /**
     * Handles the transition from the current era to the new era by performing any necessary updates
     * to the game state and returning the new era.
     * @param game the current game state, which may be modified during the era transition process.
     * @param newEra the era to which the game is transitioning, which may require specific actions to be taken during the transition.
     * @return the new era after performing the necessary updates to the game state during the transition process.
     */
    public abstract Era eraTransition(Game game, Era newEra);

    /**
     * Returns the name of the era as a string,
     * which can be used for display purposes or to identify the era in other parts of the code.
     * @return
     */
    public abstract String getName();
    public abstract void selfAdd(List<BuildingCard> deckCompositionEra1, List<BuildingCard> deckCompositionEra2, List<BuildingCard> deckCompositionEra3, BuildingCard card, int numPlayers);
    public abstract BuildingDeck selectBuildingDeck(BuildingDeck buildingDecksEra1, BuildingDeck buildingDecksEra2, BuildingDeck buildingDecksEra3);
}

