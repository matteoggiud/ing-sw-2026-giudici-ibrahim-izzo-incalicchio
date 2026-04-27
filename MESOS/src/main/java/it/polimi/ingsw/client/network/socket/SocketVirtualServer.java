package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.controller.VirtualServer;
import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.client.network.socket.messages.game.CreateGameMessage;
import it.polimi.ingsw.client.network.socket.messages.game.JoinGameMessage;
import it.polimi.ingsw.client.network.socket.messages.login.LoginSubmitMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterSubmitMessage;

public class SocketVirtualServer implements VirtualServer {

    private final SocketClient socketClient;

    public SocketVirtualServer(SocketClient socketClient) {
        this.socketClient = socketClient;
    }


    /**
     * @param message
     */
    @Override
    public void sendMessage(ClientMessage message) {
        socketClient.sendMessage(message);
    }

    /**
     *
     */
    @Override
    public void disconnect() {
        socketClient.disconnect();
    }
}
