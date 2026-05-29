package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.GameInfoDTO;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

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
