package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;


public class SocketVirtualServer implements VirtualServer {

    private final SocketServerConnection socketServerConnection;

    public SocketVirtualServer(SocketServerConnection socketServerConnection) {
        this.socketServerConnection = socketServerConnection;
    }


    /**
     * @param message
     */
    @Override
    public void sendMessage(ClientMessage message) {
        socketServerConnection.sendMessage(message);
    }

}
