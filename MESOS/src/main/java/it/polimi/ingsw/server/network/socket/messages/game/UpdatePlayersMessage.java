package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

import java.util.List;

public class UpdatePlayersMessage extends ServerMessage {
    private final List<String> playerNicknames;

    public UpdatePlayersMessage(List<String> playerNicknames) {
        this.playerNicknames = playerNicknames;
    }

    public List<String> getPlayerNicknames() {
        return playerNicknames;
    }

}
