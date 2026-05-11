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

public class Era1 extends Era{

    private final static Map<Integer, Integer> numPlayersBuildingLimit = StandardRuleSet.getEra1BuildingLimits();
    private final static int order = 1;
    private final static String name = "Era I";

    public Era1() {}

    @Override
    public String getName() {
        return name;
    }


    @Override
    public void selfSortByEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents) {
        eraI.add(tribeCard);
    }

    @Override
    public Era handleDrawnEra(Game game, Era drawnEra) {
        if (drawnEra == null) {
            throw new IllegalArgumentException("Drawn era cannot be null.");
        }
        return drawnEra.acceptTransition(game, this);
    }

    @Override
    protected Era acceptTransition(Game game, Era1 currentEra) {
        return stayIn(currentEra);
    }

    @Override
    protected Era acceptTransition(Game game, Era2 currentEra) {
        return stayIn(currentEra);
    }

    @Override
    protected Era acceptTransition(Game game, Era3 currentEra) {
        return stayIn(currentEra);
    }

    @Override
    public HashMap<Era, BuildingDeck> createBuildingDeck(HashMap<Era, BuildingDeck> buildingDecks) {
        buildingDecks.put(this, new BuildingDeck());
        return buildingDecks;
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
