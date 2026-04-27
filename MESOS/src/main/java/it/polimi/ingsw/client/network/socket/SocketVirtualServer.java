package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.controller.VirtualServer;
import it.polimi.ingsw.client.network.socket.messages.ClientMessage;


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

}
