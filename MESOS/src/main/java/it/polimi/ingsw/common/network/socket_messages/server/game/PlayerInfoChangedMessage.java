package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.visitors.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.PlayerInfoDTO;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

import java.util.List;

public class PlayerInfoChangedMessage extends ServerMessage {
    private final int gameId;
    private final List<PlayerInfoDTO> playerInfos;

    public PlayerInfoChangedMessage(int gameId, List<PlayerInfoDTO> playerInfos) {
        this.gameId = gameId;
        this.playerInfos = playerInfos;
    }

    public List<PlayerInfoDTO> getPlayerInfos() {
        return playerInfos;
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
