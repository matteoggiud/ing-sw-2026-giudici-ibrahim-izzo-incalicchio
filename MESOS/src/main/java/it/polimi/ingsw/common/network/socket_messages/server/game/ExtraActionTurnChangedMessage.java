package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;
import it.polimi.ingsw.common.network.socket_messages.visitors.ServerMessageVisitor;

/**
 * Message indicating that the turn has changed during an extra action phase.
 */
public class ExtraActionTurnChangedMessage extends ServerMessage {
    private final int gameId;
    private final String currentPlayerNickname;

    /**
     * Creates a new ExtraActionTurnChangedMessage.
     * @param gameId the ID of the game
     * @param currentPlayerNickname the nickname of the current player
     */
    public ExtraActionTurnChangedMessage(int gameId, String currentPlayerNickname) {
        this.gameId = gameId;
        this.currentPlayerNickname = currentPlayerNickname;
    }

    /**
     * Dispatches this message to the appropriate method in the ServerMessageVisitor.
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }

    /**
     * Returns the ID of the game.
     * @return the game ID
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Returns the nickname of the current player.
     * @return the current player nickname
     */
    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }
}
