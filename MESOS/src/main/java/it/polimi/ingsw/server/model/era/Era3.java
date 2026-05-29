package it.polimi.ingsw.server.model.era;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.core.Board;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.deck.BuildingDeck;
import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing the third era of the game,
 */
public class Era3 extends Era {

    /**
     * A map that defines the building limits for the number of players in Era III.
     */
    private final static Map<Integer, Integer> numPlayersBuildingLimit = StandardRuleSet.getEra3BuildingLimits();

    /**
     * The order of the era for sorting purposes, with Era III having the highest order of 3.
     */
    private final static int order = 3;

    /**
     * The name of the era as a string.
     */
    private final static String name = "Era III";

    /**
     * Constructor for the Era3 class, which initializes the era with its specific properties and behavior.
     */
    public Era3() {
    }

    /**
     * Returns the name of the era as a string.
     * @return the name of the era as a string.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the order of the era.
     * @return the order of the era.
     */
    @Override
    public int getOrder() {
        return order;
    }

    /**
     * Sorts the given tribe card into the appropriate era list based on its era and type.
     * @param tribeCard the tribe card to be sorted into the correct era list.
     * @param eraI the list of tribe cards belonging to Era I.
     * @param eraII the list of tribe cards belonging to Era II.
     * @param eraIII the list of tribe cards belonging to Era III.
     * @param finalEvents the list of event cards that are added to the final events list, if applicable.
     */
    @Override
    public void selfSortByEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents) {
        if (tribeCard.isFinalEvent()) {
            finalEvents.add(tribeCard);
        } else {
            eraIII.add(tribeCard);
        }
    }

    /**
     * Handles the transition from the current era to the new era by performing any necessary updates
     * @param game the current game state, which may be modified during the era transition process.
     * @param newEra the era to which the game is transitioning, which may require specific actions to be taken during the transition.
     * @return the new era after performing the necessary updates to the game state during the transition process.
     */
    @Override
    public Era eraTransition(Game game, Era newEra) {
        if (newEra == null) {
            throw new IllegalArgumentException("New era cannot be null");
        }

        Era currentEra = game.getCurrentEra();

        if (newEra.getOrder() <= currentEra.getOrder()) {
            return currentEra;
        }

        Board board = game.getBoard();
        game.setCurrentEra(newEra);
        board.clearLowerBuildingRow();
        board.transferUpperBuildingToLower();
        board.placeInUpperBuildingRow(game.getBuildingDecks().getBuildingDeck(newEra));

        return newEra;
    }

    /**
     * Adds the given building card to the appropriate era list based on the current era and the number of players in the game.
     * @param deckCompositionEra1 the list of building cards belonging to Era I, which may be modified by adding the given card if it belongs to this era.
     * @param deckCompositionEra2 the list of building cards belonging to Era II, which may be modified by adding the given card if it belongs to this era.
     * @param deckCompositionEra3 the list of building cards belonging to Era III, which may be modified by adding the given card if it belongs to this era.
     * @param card the building card to be added to the appropriate era list based on the current era and the number of players in the game.
     * @param numPlayers the number of players in the game, which may affect which era list the given card is added to based on the current era and the card's properties.
     */
    @Override
    public void selfAdd(List<BuildingCard> deckCompositionEra1, List<BuildingCard> deckCompositionEra2, List<BuildingCard> deckCompositionEra3, BuildingCard card, int numPlayers) {
        if (deckCompositionEra3.size() < numPlayersBuildingLimit.get(numPlayers)) {
            deckCompositionEra3.add(card);
        }
    }

    /**
     * Selects the appropriate building deck for the current era based on the given building decks for each era.
     * @param buildingDecksEra1 the building deck for Era I, which may be selected if the current era is Era I.
     * @param buildingDecksEra2 the building deck for Era II, which may be selected if the current era is Era II.
     * @param buildingDecksEra3 the building deck for Era III, which may be selected if the current era is Era III.
     * @return the building deck that corresponds to the current era.
     */
    @Override
    public BuildingDeck selectBuildingDeck(BuildingDeck buildingDecksEra1, BuildingDeck buildingDecksEra2, BuildingDeck buildingDecksEra3) {
        return buildingDecksEra3;
    }
}
