package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.server.controller.VirtualView;
import it.polimi.ingsw.server.model.observer.dto.BoardDTO;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerDeckDTO;
import it.polimi.ingsw.server.model.observer.dto.mapper.GameInfoDTOMapper;
import it.polimi.ingsw.server.model.observer.event.*;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;
import it.polimi.ingsw.server.network.socket.messages.game.*;


import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class SocketVirtualView implements VirtualView {

    private final ObjectOutputStream outputStream;

    public SocketVirtualView(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Sends a message to the client through the socket connection.
     *
     * @param message the message to be sent to the client
     */
    @Override
    public void sendMessage(ServerMessage message) throws RuntimeException {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message to client: " + e.getMessage(), e);
        }
    }

    /**
     * @param event the event containing information about the started game
     */
    @Override
    public void update(GameStartedEvent event) {
        int gameId = event.getGameId();
        List<String> playerNicknames = event.getPlayerNicknames();

        GameStartedMessage message = new GameStartedMessage(gameId, playerNicknames);

        sendMessage(message);
    }

    /**
     * @param event the event containing the updated player list
     */
    @Override
    public void update(PlayersChangedEvent event) {
        List<String> playerNicknames = event.getPlayerNicknames();

        UpdatePlayersMessage message = new UpdatePlayersMessage(playerNicknames);

        sendMessage(message);
    }

    /**
     * @param event the event containing updated board information
     */
    @Override
    public void update(BoardUpdatedEvent event) {
        BoardDTO board = event.getBoard();
        int gameId = event.getGameId();

        BoardUpdatedMessage message = new BoardUpdatedMessage(gameId, board);

        sendMessage(message);
    }

    /**
     * @param event the event containing the new era
     */
    @Override
    public void update(EraChangedEvent event) {

        EraChangedMessage message = new EraChangedMessage();

        sendMessage(message);
    }

    /**
     * @param event the event containing the current player for the placing totem phase
     */
    @Override
    public void update(PlaceTotemTurnChangedEvent event) {
        int gameId = event.getGameId();
        String currentPlayerNickname = event.getCurrentPlayerNickname();

        PlaceTotemTurnChangedMessage message = new PlaceTotemTurnChangedMessage(gameId, currentPlayerNickname);

        sendMessage(message);

    }

    /**
     * @param event the event containing the current player for the action resolution phase
     */
    @Override
    public void update(ResolveActionTurnChangedEvent event) {
        int gameId = event.getGameId();
        String currentPlayerNickname = event.getCurrentPlayerNickname();

        ResolveActionTurnChangedMessage message = new ResolveActionTurnChangedMessage(gameId, currentPlayerNickname);

        sendMessage(message);

    }

    /**
     * @param event the event containing the current player for the extra action phase
     */
    @Override
    public void update(ExtraActionTurnChangedEvent event) {
        int gameId = event.getGameId();
        String currentPlayerNickname = event.getCurrentPlayerNickname();

        ExtraActionTurnChangedMessage message = new ExtraActionTurnChangedMessage(gameId, currentPlayerNickname);

        sendMessage(message);
    }

    /**
     * @param event the event containing the updated resources of one player
     */
    @Override
    public void update(PlayerResourcesUpdatedEvent event) {
        int gameId = event.getGameId();
        String playerNickname = event.getPlayerNickname();
        int food = event.getFood();
        int prestigePoints = event.getPrestigePoints();

        PlayerResourcesUpdatedMessage message = new PlayerResourcesUpdatedMessage(gameId, playerNickname, food, prestigePoints);

        sendMessage(message);
    }

    /**
     * @param event the event containing the updated personal deck of one player
     */
    @Override
    public void update(PlayerDeckUpdatedEvent event) {
        int gameId = event.getGameId();
        String playerNickname = event.getPlayerNickname();
        PlayerDeckDTO playerDeck = event.getPlayerDeck();

        PlayerDeckUpdatedMessage message = new PlayerDeckUpdatedMessage(gameId, playerNickname, playerDeck);

        sendMessage(message);
    }

    /**
     * @param event the event containing the final ranking
     */
    @Override
    public void update(GameEndedEvent event) {
        int gameId = event.getGameId();
        Map<String, Integer> finalRanking = event.getFinalRanking();

        GameEndedMessage message = new GameEndedMessage(gameId, finalRanking);

        sendMessage(message);

    }

    /**
     *
     * @param event the event containing information about the joined game and the player who joined
     */
    @Override
    public void update(JoinedGameEvent event) {
        GameInfoMessage message = new GameInfoMessage(event.getGameInfo());

        sendMessage(message);
    }

    /**
     *
     * @param event the event containing information about the left game and the player who left
     */
    @Override
    public void update(PlayerInfoChangedEvent event) {
        PlayerInfoChangedMessage message = new PlayerInfoChangedMessage(event.getGameId(), event.getPlayerInfo());

        sendMessage(message);
    }
}
