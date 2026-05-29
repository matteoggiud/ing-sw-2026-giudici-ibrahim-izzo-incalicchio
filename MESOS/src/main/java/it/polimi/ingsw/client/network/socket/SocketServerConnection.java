package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.client.ServerMessageVisitor;
import it.polimi.ingsw.client.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServerConnection {
    private final String host;
    private final int port;
    private final ServerMessageVisitor serverVisitor;
    private final ClientState clientState;
    private final ClientView clientView;

    private SocketServerListener clientHandler;

    public SocketServerConnection(String host, int port, ServerMessageVisitor serverVisitor, ClientState clientState, ClientView clientView) {
        this.host = host;
        this.port = port;
        this.serverVisitor = serverVisitor;
        this.clientState = clientState;
        this.clientView = clientView;
    }

    /**
     * Opens the socket connection and starts the server receiver handler thread.
     */
    public void connect() throws IOException {
        Socket socket = new Socket(host, port);

        // Keep same stream creation order used on server side
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        clientState.connect();
        clientHandler = new SocketServerListener(socket, inputStream, outputStream, serverVisitor, clientState, clientView);
        Thread clientHandlerThread = new Thread(clientHandler, "socket-client-handler");
        clientHandlerThread.start();
    }

    /**
     * Sends a client message to the server.
     */
    public void sendMessage(ClientMessage message) {
        if (clientHandler == null) {
            throw new IllegalStateException("Client is not connected.");
        }
        clientHandler.sendMessage(message);
    }

}
