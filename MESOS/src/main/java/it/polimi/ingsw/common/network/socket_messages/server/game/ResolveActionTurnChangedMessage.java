package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

/**
 * Message indicating that the turn has changed during the resolve action phase.
 */
public class ResolveActionTurnChangedMessage extends ServerMessage {

    private final int gameId;
    private final String currentPlayerNickname;

    /**
     * Creates a new ResolveActionTurnChangedMessage.
     *
     * @param gameId                 the ID of the game
     * @param currentPlayerNickname  the nickname of the current player
     */
    public ResolveActionTurnChangedMessage(int gameId, String currentPlayerNickname) {
        this.gameId = gameId;
        this.currentPlayerNickname = currentPlayerNickname;
    }

    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }

    public int getGameId() {
        return gameId;
    }

    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }
}


