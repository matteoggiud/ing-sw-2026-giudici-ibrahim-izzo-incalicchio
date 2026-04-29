package it.polimi.ingsw.server.model.era;

import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;

import java.util.List;
import java.util.Map;

public class Era2 extends Era{

    private final static Map<Integer, Integer> numPlayersBuildingLimit = StandardRuleSet.getEra2BuildingLimits();

    public Era2() {}

    @Override
    public void selfSortbyEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents) {
        eraII.add(tribeCard);
    }

    @Override
    public int getBuildingLimit(int numPlayers) {
        return  numPlayersBuildingLimit.get(numPlayers);
    }
}
