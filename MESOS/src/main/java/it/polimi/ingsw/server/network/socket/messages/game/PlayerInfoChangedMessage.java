package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

public class PlayerInfoChangedMessage extends ServerMessage {
    private final int gameId;
    private final PlayerInfoDTO playerInfo;

    public PlayerInfoChangedMessage(int gameId, PlayerInfoDTO playerInfo) {
        this.gameId = gameId;
        this.playerInfo = playerInfo;
    }

    public PlayerInfoDTO getPlayerInfo() {
        return playerInfo;
    }

    public int getGameId() {
        return gameId;
    }


    /**
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);

    }
}
