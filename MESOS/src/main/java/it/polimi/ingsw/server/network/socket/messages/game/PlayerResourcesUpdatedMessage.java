package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.server.network.socket.messages.ServerMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;

/**
 * Message indicating that a player's resources have been updated.
 * This message is sent to all clients whenever a player's resources change,
 * such as after performing an action or at the end of a turn.
 */
public class PlayerResourcesUpdatedMessage extends ServerMessage {
    private final int gameId;
    private final String playerNickname;
    private final int food;
    private final int prestigePoints;

    /**
     * Creates a new PlayerResourcesUpdatedMessage.
     * @param gameId the ID of the game
     * @param playerNickname the nickname of the player whose resources have been updated
     * @param food the player's current food amount
     * @param prestigePoints the player's current prestige points amount
     */
    public PlayerResourcesUpdatedMessage(int gameId, String playerNickname, int food, int prestigePoints) {
        this.gameId = gameId;
        this.playerNickname = playerNickname;
        this.food = food;
        this.prestigePoints = prestigePoints;
    }

    /**
     * Dispatches this message to the appropriate method in the ServerMessageVisitor.
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }

    /**
     * Returns the ID of the game.
     * @return the game ID
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Returns the nickname of the player whose resources have been updated.
     * @return the player nickname
     */
    public String getCurrentPlayerNickname() {
        return playerNickname;
    }

    /**
     * Returns the player's current food amount.
     * @return the player's food amount
     */
    public int getFood() {
        return food;
    }

    /**
     * Returns the player's current prestige points amount.
     * @return the player's prestige points amount
     */
    public int getPrestigePoints() {
        return prestigePoints;
    }
}
