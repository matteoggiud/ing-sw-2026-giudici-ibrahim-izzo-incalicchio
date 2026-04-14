package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.card.Era;
import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.TribeCard;
import it.polimi.ingsw.model.core.Board;
import it.polimi.ingsw.model.core.*;
import it.polimi.ingsw.model.gamelogic.EffectResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Extra action -> Eventi + Modifiers ->
 */
public class EndRoundState implements IGameState {
    @Override
    public void nextState(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }

        if (game.isLastRoundTriggered()) {
            game.setGameState(new FinalRoundState());
            return;
        }

        game.setGameState(new PlacingTotemState());
    }

    @Override
    public void executePhase(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }

        initializeCurrentEraIfNeeded(game);

        List<EventCard> eventsToResolve = collectEventsToResolve(game);
        if (!eventsToResolve.isEmpty()) {
            new EffectResolver(game).resolveEvents(eventsToResolve);
        }

        if (game.isLastRound()) {
            game.setLastRoundTriggered(true);
            nextState(game);
            return;
        }

        discardLowerRowTribeCards(game.getBoard());
        game.getBoard().transferUpperTribeToLower();
        refillUpperRow(game);

        nextState(game);
    }

    private List<EventCard> collectEventsToResolve(Game game) {
        List<EventCard> events = new ArrayList<>(game.getBoard().getLowerRow().getTribeRow().getEventCards());

        if (game.isLastRound()) {
            events.addAll(game.getBoard().getUpperRow().getTribeRow().getEventCards());
        }

        return events;
    }

    private void discardLowerRowTribeCards(Board board) {
        board.getLowerRow().getTribeRow().clear();
    }

    private void refillUpperRow(Game game) {
        Board board = game.getBoard();
        int targetUpperRowSize = game.getRuleSet().getUpperRowSize(game.getNumberOfPlayers());
        Era currentEra = initializeCurrentEraIfNeeded(game);

        while (board.getUpperRow().getTribeRow().count() < targetUpperRowSize) {
            TribeCard drawnCard = game.getTribeDeck().drawCard();
            board.getUpperRow().addCard(drawnCard);

            if (drawnCard.getEra().ordinal() > currentEra.ordinal()) {
                currentEra = handleEraTransition(game, drawnCard.getEra());
            }
        }
    }

    private Era handleEraTransition(Game game, Era newEra) {
        if (newEra == null) {
            throw new IllegalArgumentException("New era cannot be null");
        }

        Era currentEra = initializeCurrentEraIfNeeded(game);
        if (newEra.ordinal() <= currentEra.ordinal()) {
            return currentEra;
        }

        Board board = game.getBoard();
        game.setCurrentEra(newEra);

        if (newEra == Era.ERA_3) {
            board.clearLowerBuildingRow();
        }

        board.transferUpperBuildingToLower();
        board.placeInUpperBuildingRow(game.getBuildingDecks().getBuildingDeck(newEra));

        return newEra;
    }

    private Era initializeCurrentEraIfNeeded(Game game) {
        try {
            return game.getCurrentEra();
        } catch (IllegalStateException ignored) {
            game.setCurrentEra(Era.ERA_1);
            return Era.ERA_1;
        }
    }
}