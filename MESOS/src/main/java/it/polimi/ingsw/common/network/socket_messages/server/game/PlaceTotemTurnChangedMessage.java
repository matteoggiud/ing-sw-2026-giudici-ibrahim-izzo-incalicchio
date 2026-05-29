package it.polimi.ingsw.common.network.socket_messages.server.game;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;
import it.polimi.ingsw.common.network.socket_messages.visitors.ServerMessageVisitor;

/**
 * This class represents a message sent by the server to all clients when the turn changes to the next player in the game.
 * It contains the id of the game and the nickname of the player whose turn it is now.
 */
public class PlaceTotemTurnChangedMessage extends ServerMessage {
    private final int gameId;
    private final String currentPlayerNickname;

    /**
     * Creates a new PlaceTotemTurnChangedMessage, which indicates that the turn has changed to the next player in the game. 
     * This message is sent to all clients whenever the turn changes.
     * @param gameId the id of the game in which the turn has changed
     * @param currentPlayerNickname the nickname of the player whose turn it is now
     */
    public PlaceTotemTurnChangedMessage(int gameId, String currentPlayerNickname) {
        this.gameId = gameId;
        this.currentPlayerNickname = currentPlayerNickname;
    }
    
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }

    /**
     * Returns the id of the game in which the turn has changed.
     * @return the game id
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Returns the nickname of the player whose turn it is now.
     * @return the current player nickname
     */
    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }
}
