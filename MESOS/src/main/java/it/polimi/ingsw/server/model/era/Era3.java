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

public class Era3 extends Era {
    private final static Map<Integer, Integer> numPlayersBuildingLimit = StandardRuleSet.getEra3BuildingLimits();
    private final static int order = 3;
    private final static String name = "Era III";

    public Era3() {}

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
        if(tribeCard.isFinalEvent()){
            finalEvents.add(tribeCard);
        }else{
            eraIII.add(tribeCard);
        }
    }

    public static int getBuildingLimit(int numPlayers) {
        return numPlayersBuildingLimit.get(numPlayers);
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
        board.clearLowerBuildingRow();
        board.transferUpperBuildingToLower();
        board.placeInUpperBuildingRow(game.getBuildingDecks().getBuildingDeck(newEra));

        return newEra;
    }

    @Override
    public void selfAdd(List<BuildingCard> deckCompositionEra1, List<BuildingCard> deckCompositionEra2, List<BuildingCard> deckCompositionEra3, BuildingCard card, int numPlayers) {
        if(deckCompositionEra3.size() < numPlayersBuildingLimit.get(numPlayers)) {}
        deckCompositionEra3.add(card);
    }
}
