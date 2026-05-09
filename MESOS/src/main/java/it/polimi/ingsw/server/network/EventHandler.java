package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.dto.BoardDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerDeckDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;
import it.polimi.ingsw.server.model.observer.event.*;
import it.polimi.ingsw.server.network.SessionRegistry;
import it.polimi.ingsw.server.network.socket.messages.game.*;


import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

/**
 * The EventHandler class implements the IGameObserver interface and serves as a bridge between the game model and the virtual view.
 * It listens for various game events and updates the virtual view accordingly by sending appropriate messages to the clients.
 */
public class EventHandler implements IGameObserver {

    private final GenericVirtualView view;

    public EventHandler(GenericVirtualView view) {
        this.view = view;
    }

    public GenericVirtualView getView() {
        return view;
    }

    /**
     * @param event the event containing information about the started game
     */
    @Override
    public void update(GameStartedEvent event) {
        int gameId = event.getGameId();
        List<String> playerNicknames = event.getPlayerNicknames();

        view.onGameStarted(gameId, playerNicknames);
    }

    /**
     * @param event the event containing the updated player list
     */
    @Override
    public void update(PlayersChangedEvent event) {
        List<String> playerNicknames = event.getPlayerNicknames();

        view.onPlayerChanged(playerNicknames);
    }

    /**
     * @param event the event containing updated board information
     */
    @Override
    public void update(BoardUpdatedEvent event) {
        BoardDTO board = event.getBoard();
        int gameId = event.getGameId();

        view.onBoardChanged(gameId, board);
    }

    /**
     * @param event the event containing the new era
     */
    @Override
    public void update(EraChangedEvent event) {

        EraChangedMessage message = new EraChangedMessage(event.getGameId(), event.getNewEra());

        view.onEraChanged(event.getGameId(), event.getNewEra());
    }

    /**
     * @param event the event containing the current player for the placing totem phase
     */
    @Override
    public void update(PlaceTotemTurnChangedEvent event) {
        int gameId = event.getGameId();
        String currentPlayerNickname = event.getCurrentPlayerNickname();

        view.onPlaceTotemTurnChanged(gameId, currentPlayerNickname);
    }

    /**
     * @param event the event containing the current player for the action resolution phase
     */
    @Override
    public void update(ResolveActionTurnChangedEvent event) {
        int gameId = event.getGameId();
        String currentPlayerNickname = event.getCurrentPlayerNickname();

        view.onResolveActionTurnChanged(gameId, currentPlayerNickname);
    }

    /**
     * @param event the event containing the current player for the extra action phase
     */
    @Override
    public void update(ExtraActionTurnChangedEvent event) {
        int gameId = event.getGameId();
        String currentPlayerNickname = event.getCurrentPlayerNickname();

        view.onExtraActionTurnChanged(gameId, currentPlayerNickname);
    }

    /**
     * @param event the event containing the updated resources of one player
     */
    @Override
    public void update(PlayerResourcesUpdatedEvent event) {
        /*int gameId = event.getGameId();
        String playerNickname = event.getPlayerNickname();
        int food = event.getFood();
        int prestigePoints = event.getPrestigePoints();

        view.onPlayerInfosChanged(gameId, List.of(new PlayerInfoDTO(playerNickname, food, prestigePoints, null, null)));*/
    }

    /**
     * @param event the event containing the updated personal deck of one player
     */
    @Override
    public void update(PlayerDeckUpdatedEvent event) {
        int gameId = event.getGameId();
        String playerNickname = event.getPlayerNickname();
        PlayerDeckDTO playerDeck = event.getPlayerDeck();

        view.onPlayerDeckUpdated(gameId, playerNickname, playerDeck);
    }

    /**
     * @param event the event containing the final ranking
     */
    @Override
    public void update(GameEndedEvent event) {
        int gameId = event.getGameId();
        Map<String, Integer> finalRanking = event.getFinalRanking();

        view.onGameEnded(gameId, finalRanking, event.getNumPlayers(), event.getHistoricalLeaderboard());

        SessionRegistry.getInstance().clearActiveGameIdForGame(gameId);
    }

    /**
     *
     * @param event the event containing information about the joined game and the player who joined
     */
    @Override
    public void update(JoinedGameEvent event) {
        GameInfoMessage message = new GameInfoMessage(event.getGameInfo());

        view.onJoinedGame(event.getGameInfo());
    }

    /**
     *
     * @param event the event containing information about the left game and the player who left
     */
    @Override
    public void update(PlayerInfosChangedEvent event) {
        PlayerInfoChangedMessage message = new PlayerInfoChangedMessage(event.getGameId(), event.getPlayerInfos());

        view.onPlayerInfosChanged(event.getGameId(), event.getPlayerInfos());
    }

    /**
     *
     * @param resolvingEventUpdate the event containing information about the event card being resolved and the current state of the resolution process
     */
    @Override
    public void update(ResolvingEventUpdate resolvingEventUpdate) {
        ResolvingEventMessage message = new ResolvingEventMessage(resolvingEventUpdate.getGameId(), resolvingEventUpdate.getEventCard());

        view.onResolvingEvent(resolvingEventUpdate.getGameId(), resolvingEventUpdate.getEventCard());
    }
}
