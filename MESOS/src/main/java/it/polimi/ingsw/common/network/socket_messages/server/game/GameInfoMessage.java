package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.client.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.GameInfoDTO;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

public class GameInfoMessage extends ServerMessage {
    private final GameInfoDTO gameInfo;

    public GameInfoMessage(GameInfoDTO gameInfo) {
        this.gameInfo = gameInfo;
    }

    public GameInfoDTO getGameInfo() {
        return gameInfo;
    }

    /**
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }
}
