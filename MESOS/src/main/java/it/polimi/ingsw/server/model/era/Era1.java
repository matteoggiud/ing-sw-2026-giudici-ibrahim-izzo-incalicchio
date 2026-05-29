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
 * Class representing the first era of the game,
 * which implements the Era interface and defines the specific behavior and properties of Era I.
 */
public class Era1 extends Era{

    /**
     * A map that defines the building limits for each number of players in Era I,
     *
     */
    private final static Map<Integer, Integer> numPlayersBuildingLimit = StandardRuleSet.getEra1BuildingLimits();

    /**
     * The order of the era for sorting purposes, with Era I having the lowest order of 1.
     */
    private final static int order = 1;

    /**
     * The name of the era as a string.
     */
    private final static String name = "Era I";

    /**
     * Constructor for the Era1 class, which initializes the era with its specific properties and behavior.
     */
    public Era1() {}

    /**
     * Returns the name of the era as a string.
     * @return the name of the era as a string.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *  Returns the order of the era.
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
        eraI.add(tribeCard);
    }

    /**
     * Creates a building deck for Era I by adding the appropriate building cards to the given building decks map.
     * @param game the current game.
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
        if(deckCompositionEra1.size() < numPlayersBuildingLimit.get(numPlayers)) {
            deckCompositionEra1.add(card);
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
        return buildingDecksEra1;
    }
}
