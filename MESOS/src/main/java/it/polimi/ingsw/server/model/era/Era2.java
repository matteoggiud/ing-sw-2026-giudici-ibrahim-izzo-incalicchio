package it.polimi.ingsw.server.model.era;

import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.core.Board;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.deck.BuildingDeck;
import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Era2 extends Era{

    private final static Map<Integer, Integer> numPlayersBuildingLimit = StandardRuleSet.getEra2BuildingLimits();
    private final static int order = 2;
    private final static String name = "Era II";

    public Era2() {}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void selfSortbyEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents) {
        eraII.add(tribeCard);
    }

    @Override
    public int getBuildingLimit(int numPlayers) {
        return  numPlayersBuildingLimit.get(numPlayers);
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
}
