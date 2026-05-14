package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.core.Board;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.gamelogic.EffectResolver;
import it.polimi.ingsw.server.model.observer.dto.GameSnapshotDTO;
import it.polimi.ingsw.server.model.observer.dto.mapper.BoardDTOMapper;
import it.polimi.ingsw.server.model.observer.dto.mapper.GameSnapshotDTOMapper;
import it.polimi.ingsw.server.model.observer.dto.mapper.PlayerInfosDTOMapper;
import it.polimi.ingsw.server.model.observer.event.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the end round phase of the game.
 */
public class EndRoundState implements IGameState {

    @Override
    public void nextState(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }

        if (game.isLastRoundTriggered())
            game.setGameState(new ComputeFinalScoreState());
        else
            game.setGameState(new PlacingTotemState());

        game.getGameState().executePhase(game);
    }

    /**
     * Executes the end round phase of the game.
     * It resolves event cards, updates player resources if needed,
     * refreshes the board, and prepares the next round.
     *
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        // First of all: Save the game snapshot for server persistance
        GameSnapshotDTO snapshotDTO = GameSnapshotDTOMapper.toGameSnapshotDTO(game);
        new DataWriting().updateGameSnapshot(game.getGameId(), snapshotDTO);

        if (game.isLastRound()) {
            game.setLastRoundTriggered(true);
        }

        List<EventCard> eventsToResolve = collectEventsToResolve(game);
        if (!eventsToResolve.isEmpty()) {
            new EffectResolver(game).resolveEvents(eventsToResolve);
        }


        if (game.isLastRoundTriggered()) {
            nextState(game);
            return;
        }

        discardLowerRowTribeCards(game.getBoard());
        game.getBoard().transferUpperTribeToLower();
        refillUpperRow(game);

        notifyBoardUpdate(game);
        nextState(game);
        notifyPlayerInfos(game);
    }

    private void notifyPlayerInfos(Game game) {
        game.getObserverManager().notifyObservers(new PlayerInfosChangedEvent(
                game.getGameId(),
                PlayerInfosDTOMapper.toPlayerInfoDTOList(game.getPlayers().getPlayers()
                )));
    }

    /**
     * Collects all the event cards that need to be resolved at the end of the round.
     *
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
     * Discards all the tribe cards in the lower row of the board.
     *
     * @param board the game board
     */
    private void discardLowerRowTribeCards(Board board) {
        board.getLowerRow().getTribeRow().clear();
    }

    /**
     * Refills the upper row of the board with new tribe cards drawn from the tribe deck.
     *
     * @param game the current game context
     */
    private void refillUpperRow(Game game) {
        Board board = game.getBoard();
        int targetUpperRowSize = game.getRuleSet().getUpperRowSize(game.getNumberOfPlayers());
        Era currentEra = game.getCurrentEra();

        while (board.getUpperRow().getTribeRow().count() < targetUpperRowSize) {
            TribeCard drawnCard = game.getTribeDeck().drawCard();
            board.getUpperRow().addCard(drawnCard);

            if (drawnCard.getEra().getOrder() > currentEra.getOrder()) {
                currentEra = handleEraTransition(game, drawnCard.getEra());
            }
        }
    }

    /**
     * Handles the transition between eras when a new era is reached.
     *
     * @param game   the current game context
     * @param newEra the new era that has been reached
     * @return the current era after handling the transition
     */
    private Era handleEraTransition(Game game, Era newEra) {

        newEra = newEra.eraTransition(game, newEra);

        // Notify observers about the era change and the updated board state after the transition
        notifyEraChanged(game, newEra);
        notifyBoardUpdate(game);

        return newEra;
    }


    /**
     * Notifies observers about the updated board state.
     *
     * @param game the current game context
     */
    private void notifyBoardUpdate(Game game) {
        game.getObserverManager().notifyObservers(
                new BoardUpdatedEvent(game.getGameId(), BoardDTOMapper.toBoardDTO(game))
        );
    }


    /**
     * Notifies observers that the game reached a new era.
     *
     * @param game   the current game context
     * @param newEra the new era reached by the game
     */
    private void notifyEraChanged(Game game, Era newEra) {
        game.getObserverManager().notifyObservers(
                new EraChangedEvent(game.getGameId(), newEra.getName())
        );
    }

    @Override
    public boolean isEnded() {
        return false;
    }

}
