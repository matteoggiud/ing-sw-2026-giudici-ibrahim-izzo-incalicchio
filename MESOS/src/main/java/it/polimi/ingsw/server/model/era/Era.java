package it.polimi.ingsw.server.model.era;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.core.Board;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.deck.BuildingDeck;

import java.util.HashMap;
import java.util.List;

/**
 * Enumeration representing the three eras in the game: Era I, Era II, and Era III.
 */
public abstract class Era {
    public abstract void selfSortByEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents);
    public abstract HashMap<Era, BuildingDeck> createBuildingDeck(HashMap<Era, BuildingDeck> buildingDecks);
    public abstract String getName();
    public abstract void selfAdd(List<BuildingCard> deckCompositionEra1, List<BuildingCard> deckCompositionEra2, List<BuildingCard> deckCompositionEra3, BuildingCard card, int numPlayers);
    public abstract BuildingDeck selectBuildingDeck(BuildingDeck buildingDecksEra1, BuildingDeck buildingDecksEra2, BuildingDeck buildingDecksEra3);

    //Visitor pattern methods for era transition
    public abstract Era handleDrawnEra(Game game, Era drawnEra);
    protected abstract Era acceptTransition(Game game, Era1 currentEra);
    protected abstract Era acceptTransition(Game game, Era2 currentEra);
    protected abstract Era acceptTransition(Game game, Era3 currentEra);

    protected Era stayIn(Era currentEra) {
        return currentEra;
    }

    protected Era moveGameToThisEra(Game game) {
        Board board = game.getBoard();

        game.setCurrentEra(this);
        beforeBuildingTransition(board);
        board.transferUpperBuildingToLower();
        board.placeInUpperBuildingRow(game.getBuildingDecks().getBuildingDeck(this));

        return this;
    }

    protected void beforeBuildingTransition(Board board) {
        // Default: no special cleanup before changing building row.
    }

}