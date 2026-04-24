package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.server.controller.VirtualView;
import it.polimi.ingsw.server.model.observer.event.*;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;
import it.polimi.ingsw.server.network.socket.messages.game.GameStartedMessage;
import it.polimi.ingsw.server.network.socket.messages.game.UpdatePlayersMessage;

import java.io.ObjectOutputStream;
import java.util.List;

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

    }

    /**
     * @param event the event containing the current player for the placing totem phase
     */
    @Override
    public void update(PlaceTotemTurnChangedEvent event) {

    }

    /**
     * @param event the event containing the current player for the action resolution phase
     */
    @Override
    public void update(ResolveActionTurnChangedEvent event) {

    }

    /**
     * @param event the event containing the updated resources of one player
     */
    @Override
    public void update(PlayerResourcesUpdatedEvent event) {

    }
}
