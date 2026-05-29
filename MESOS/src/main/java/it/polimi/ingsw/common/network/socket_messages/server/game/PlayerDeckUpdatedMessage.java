package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.PlayerDeckDTO;

/**
 * Message indicating that a player's deck has been updated.
 */
public class PlayerDeckUpdatedMessage extends ServerMessage {
    private final int gameId;
    private final String playerNickname;
    private final PlayerDeckDTO playerDeck;

    /**
     * Creates a new PlayerDeckUpdatedMessage.
     * @param gameId the ID of the game
     * @param playerNickname the nickname of the player whose deck has been updated
     * @param playerDeck the updated player deck DTO
     */
    public PlayerDeckUpdatedMessage(int gameId, String playerNickname, PlayerDeckDTO playerDeck) {
        this.gameId = gameId;
        this.playerNickname = playerNickname;
        this.playerDeck = playerDeck;
    }

    /**
     * Returns the ID of the game.
     * @return the game ID
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Returns the nickname of the player whose deck has been updated.
     * @return the player nickname
     */
    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * Returns the updated player deck DTO.
     * @return the updated player deck
     */
    public PlayerDeckDTO getPlayerDeck() {
        return playerDeck;
    }

    /**
     * Dispatches this message to the appropriate method in the ServerMessageVisitor.
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {

        serverVisitor.visit(this);
    }



}
