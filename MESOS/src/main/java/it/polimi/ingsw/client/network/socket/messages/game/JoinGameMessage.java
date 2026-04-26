package it.polimi.ingsw.client.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientMessageVisitor;

public class JoinGameMessage extends ClientMessage {
    private final String playerNickname;
    private final int gameId;

    public JoinGameMessage(String username, int gameId) {
        this.playerNickname = username;
        this.gameId = gameId;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public int getGameId() {
        return gameId;
    }

    /**
     * @param clientVisitor
     */
    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);

    }
}
