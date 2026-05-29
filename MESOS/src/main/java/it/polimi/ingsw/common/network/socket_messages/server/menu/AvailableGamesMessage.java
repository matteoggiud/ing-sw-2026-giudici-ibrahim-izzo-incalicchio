package it.polimi.ingsw.common.network.socket_messages.server.menu;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.GameInfoDTO;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

import java.util.List;

public class AvailableGamesMessage extends ServerMessage {
    private final List<GameInfoDTO> games;

    public AvailableGamesMessage(List<GameInfoDTO> games) {
        this.games = games;
    }

    public List<GameInfoDTO> getGames() {
        return games;
    }

    /**
     * Accepts a visitor that will handle the message
     *
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }


}
