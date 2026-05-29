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
     * @return the name of the era as a string.
     */
    public abstract String getName();

    /**
     * Adds the given building card to the appropriate era list
     * based on the current era and the number of players in the game.
     * @param deckCompositionEra1 the list of building cards belonging to Era I, which may be modified by adding the given card if it belongs to this era.
     * @param deckCompositionEra2 the list of building cards belonging to Era II, which may be modified by adding the given card if it belongs to this era.
     * @param deckCompositionEra3 the list of building cards belonging to Era III, which may be modified by adding the given card if it belongs to this era.
     * @param card the building card to be added to the appropriate era list based on the current era and the number of players in the game.
     * @param numPlayers the number of players in the game, which may affect which era list the given card is added to based on the current era and the card's properties.
     */
    public abstract void selfAdd(List<BuildingCard> deckCompositionEra1, List<BuildingCard> deckCompositionEra2, List<BuildingCard> deckCompositionEra3, BuildingCard card, int numPlayers);

    /**
     * Selects the appropriate building deck for the current era based on the given building decks for each era.
     * @param buildingDecksEra1 the building deck for Era I, which may be selected if the current era is Era I.
     * @param buildingDecksEra2 the building deck for Era II, which may be selected if the current era is Era II.
     * @param buildingDecksEra3 the building deck for Era III, which may be selected if the current era is Era III.
     * @return the building deck that corresponds to the current era, which may be used for drawing building cards during the game or for other purposes related to the current era's gameplay mechanics.
     */
    public abstract BuildingDeck selectBuildingDeck(BuildingDeck buildingDecksEra1, BuildingDeck buildingDecksEra2, BuildingDeck buildingDecksEra3);
}

