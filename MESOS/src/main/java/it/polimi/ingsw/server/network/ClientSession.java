package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.network.socket.VirtualView;

import java.net.Socket;

/**
 * Represents a client session in the server, encapsulating the client's socket connection, virtual view, nickname, and active game ID.
 */
public class ClientSession {
    /**
     * The socket connection to the client, used for communication between the server and the client.
     */
    private final Socket clientSocket;

    /**
     * The virtual view associated with this client session, used to send updates and messages to the client.
     */
    private final VirtualView virtualView;

    /**
     * The nickname of the client associated with this session.
     * Null until the client has successfully logged in.
     */
    private String nickname;

    /**
     * The ID of the game that the client is currently participating in.
     * Null until the client has joined a game.
     */
    private Integer activeGameId;


    /**
     * Creates a new client session with the specified socket connection and virtual view.
     *
     * @param clientSocket the socket connection to the client
     * @param virtualView  the virtual view associated with this client session
     */
    public ClientSession(Socket clientSocket, VirtualView virtualView) {
        this.clientSocket = clientSocket;
        this.virtualView = virtualView;
        this.nickname = null;
        this.activeGameId = null;
    }

    /**
     * Gets the virtual view associated with this client session.
     *
     * @return the virtual view associated with this client session
     */
    public VirtualView getVirtualView() {
        return virtualView;
    }

    /**
     * Gets the socket connection to the client associated with this session.
     *
     * @return the socket connection to the client associated with this session
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Gets the nickname of the client associated with this session.
     *
     * @return the nickname of the client associated with this session
     */
    public String getNickname() {
        if (nickname == null) {
            throw new IllegalStateException("Nickname is not set for this client session.");
        }
        return nickname;
    }

    /**
     * Sets the nickname of the client associated with this session.
     *
     * @param nickname the nickname to set for the client associated with this session
     * @throws IllegalStateException if the nickname is already set for this client session
     */
    public void setNickname(String nickname) {
        if (this.nickname != null) {
            throw new IllegalStateException("Nickname is already set for this client session.");
        }
        this.nickname = nickname;
    }

    /**
     * Clears the nickname for this client session, indicating that the client is no longer logged in.
     */
    public void clearNickname() {
        this.nickname = null;
    }

    /**
     * Checks if the client associated with this session is logged in by verifying if the nickname is set.
     *
     * @return true if the client is logged in (nickname is set), false otherwise
     */
    public boolean isLoggedIn() {
        return nickname != null;
    }

    /**
     * Gets the ID of the game that the client is currently participating in.
     *
     * @return the ID of the game that the client is currently participating in, or null if the client has not joined a game
     */
    public Integer getActiveGameId() {
        return activeGameId;
    }

    /**
     * Sets the ID of the game that the client is currently participating in.
     *
     * @param activeGameId the ID of the game to set as active for the client associated with this session
     * @throws IllegalStateException if the active game ID is already set for this client session
     */
    public void setActiveGameId(Integer activeGameId) {
        if (this.activeGameId != null) {
            throw new IllegalStateException("Active game ID is already set for this client session.");
        }
        this.activeGameId = activeGameId;
    }

    /**
     * Clears the active game ID for this client session, indicating that the client is no longer participating in any game.
     */
    public void clearActiveGameId() {
        this.activeGameId = null;
    }

    /**
     * Checks if the client associated with this session is currently participating in a game by verifying if the active game ID is set.
     *
     * @return true if the client is currently participating in a game (active game ID is set), false otherwise
     */
    public boolean isInGame() {
        return activeGameId != null;
    }


    /**
     * Disconnects the client session by closing the socket connection and clearing the nickname and active game ID.
     */
    public void disconnectSession() {
        try {
            clientSocket.close();
        } catch (Exception e) {
            // Log the exception or handle it as needed
            System.err.println("Error while closing client socket: " + e.getMessage());
        }
        this.clearNickname();
        this.clearActiveGameId();
    }
}
