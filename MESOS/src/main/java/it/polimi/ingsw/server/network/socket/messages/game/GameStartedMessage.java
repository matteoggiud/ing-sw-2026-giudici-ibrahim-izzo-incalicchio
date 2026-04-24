package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

import java.util.List;

public class GameStartedMessage extends ServerMessage {
    private final int gameId;
    private final List<String> playerNicknames;

    public GameStartedMessage(int gameId, List<String> playerNicknames) {
        this.gameId = gameId;
        this.playerNicknames = playerNicknames;
    }

    public int getGameId() {
        return gameId;
    }

    public List<String> getPlayerNicknames() {
        return playerNicknames;
    }
}
