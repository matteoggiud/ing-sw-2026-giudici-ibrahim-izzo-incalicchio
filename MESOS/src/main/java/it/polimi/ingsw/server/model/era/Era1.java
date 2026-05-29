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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void selfSortByEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents) {
        eraI.add(tribeCard);
    }

    @Override
    public HashMap<Era, BuildingDeck> createBuildingDeck(HashMap<Era, BuildingDeck> buildingDecks) {
        buildingDecks.put(this, new BuildingDeck());
        return buildingDecks;
    }

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

    @Override
    public void selfAdd(List<BuildingCard> deckCompositionEra1, List<BuildingCard> deckCompositionEra2, List<BuildingCard> deckCompositionEra3, BuildingCard card, int numPlayers) {
        if(deckCompositionEra1.size() < numPlayersBuildingLimit.get(numPlayers)) {
            deckCompositionEra1.add(card);
        }
    }

    @Override
    public BuildingDeck selectBuildingDeck(BuildingDeck buildingDecksEra1, BuildingDeck buildingDecksEra2, BuildingDeck buildingDecksEra3) {
        return buildingDecksEra1;
    }
}
