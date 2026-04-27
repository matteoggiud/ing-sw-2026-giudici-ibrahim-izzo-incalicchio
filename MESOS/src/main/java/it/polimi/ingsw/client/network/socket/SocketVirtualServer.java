package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.controller.VirtualServer;
import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.client.network.socket.messages.game.CreateGameMessage;
import it.polimi.ingsw.client.network.socket.messages.game.JoinGameMessage;
import it.polimi.ingsw.client.network.socket.messages.login.LoginRequestMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterRequestMessage;

public class SocketVirtualServer implements VirtualServer {

    private final SocketClient socketClient;

    public SocketVirtualServer(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    /**
     * @param username
     * @param passwordHash
     */
    @Override
    public void login(String username, String passwordHash) {
    	socketClient.sendMessage(new LoginRequestMessage(username, passwordHash));
    }

    /**
     * @param username
     * @param password
     */
    @Override
    public void register(String username, String password) {
        socketClient.sendMessage(new RegisterRequestMessage(username, password));
    }

    /**
     *
     */
    @Override
    public void createGame() {
        socketClient.sendMessage(new CreateGameMessage());
    }

    /**
     * @param gameId
     */
    @Override
    public void joinGame(int gameId) {
        socketClient.sendMessage(new JoinGameMessage(gameId));
    }

    /**
     *
     */
    @Override
    public void disconnect() {
        socketClient.disconnect();
    }
}
