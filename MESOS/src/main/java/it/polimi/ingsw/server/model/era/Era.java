package it.polimi.ingsw.server.model.era;

import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.deck.BuildingDeck;

import java.util.HashMap;
import java.util.List;

/**
 * Enumeration representing the three eras in the game: Era I, Era II, and Era III.
 */
public abstract class Era {
    public abstract void selfSortbyEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents);
    public abstract int getBuildingLimit(int numPlayers);
    public abstract HashMap<Era, BuildingDeck> createBuildingDeck(HashMap<Era, BuildingDeck> buildingDecks);
    public abstract int getOrder();
    public abstract Era eraTransition(Game game, Era newEra);
    public abstract String getName();
}

