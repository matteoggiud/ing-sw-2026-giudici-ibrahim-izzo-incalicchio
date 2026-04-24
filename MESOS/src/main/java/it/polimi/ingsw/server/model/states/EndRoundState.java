package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.card.Era;
import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.core.Board;
import it.polimi.ingsw.server.model.core.*;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.gamelogic.EffectResolver;

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
            game.setGameState(new ComputeFinalScoreState());
            return;
        }

        game.setGameState(new PlacingTotemState());
    }

    /**
     * This method executes the end round phase of the game,
     * it resolves all the events in the lower row and if it's the last round also the upper row,
     * then it discards all the tribe cards in the lower row,
     * transfer the upper row to the lower row and refill the upper row with new tribe cards,
     * if a new era is reached it also transfer the upper building row to the lower building row
     * and place new building cards in the upper building row.
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }

        if (game.isLastRound()) {
            game.setLastRoundTriggered(true);
        }

        List<EventCard> eventsToResolve = collectEventsToResolve(game);
        if (!eventsToResolve.isEmpty()) {
            new EffectResolver(game).resolveEvents(eventsToResolve);
        }

        if(game.isLastRoundTriggered()){
            nextState(game);
        }else{
            discardLowerRowTribeCards(game.getBoard());
            game.getBoard().transferUpperTribeToLower();
            refillUpperRow(game);
            nextState(game);
        }


    }

    /**
     * This method collects all the event cards that need to be resolved at the end of the round.
     * @param game the current game context
     * @return a list of event cards to resolve
     */
    private List<EventCard> collectEventsToResolve(Game game) {
        List<EventCard> events = new ArrayList<>(game.getBoard().getLowerRow().getTribeRow().getEventCards());

        if (game.isLastRound()) {
            events.addAll(game.getBoard().getUpperRow().getTribeRow().getEventCards());
        }

        return events;
    }

    /**
     * This method discards all the tribe cards in the lower row of the board.
     * @param board the game board
     */
    private void discardLowerRowTribeCards(Board board) {
        board.getLowerRow().getTribeRow().clear();
    }

    /**
     * This method refills the upper row of the board with new tribe cards drawn from the tribe deck.
     * @param game the current game context
     */
    private void refillUpperRow(Game game) {
        Board board = game.getBoard();
        int targetUpperRowSize = game.getRuleSet().getUpperRowSize(game.getNumberOfPlayers());
        Era currentEra = game.getCurrentEra();

        while (board.getUpperRow().getTribeRow().count() < targetUpperRowSize) {
            TribeCard drawnCard = game.getTribeDeck().drawCard();
            board.getUpperRow().addCard(drawnCard);

            if (drawnCard.getEra().ordinal() > currentEra.ordinal()) {
                currentEra = handleEraTransition(game, drawnCard.getEra());
            }
        }
    }

    /**
     * This method handles the transition between eras when a new era is reached.
     * @param game the current game context
     * @param newEra the new era that has been reached
     * @return the current era after handling the transition
     */
    private Era handleEraTransition(Game game, Era newEra) {
        if (newEra == null) {
            throw new IllegalArgumentException("New era cannot be null");
        }

        //Era currentEra = initializeCurrentEraIfNeeded(game);
        Era currentEra = game.getCurrentEra();

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


}