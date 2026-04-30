package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.client.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient {
    private final String host;
    private final int port;
    private final ServerMessageVisitor serverVisitor;
    private final ClientSessionState clientSessionState;
    private final ClientView clientView;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private SocketClientHandler clientHandler;
    private Thread clientHandlerThread;

    public SocketClient(String host, int port, ServerMessageVisitor serverVisitor, ClientSessionState clientSessionState, ClientView clientView) {
        this.host = host;
        this.port = port;
        this.serverVisitor = serverVisitor;
        this.clientSessionState = clientSessionState;
        this.clientView = clientView;
    }

    /**
     * Opens the socket connection and starts the server receiver handler thread.
     */
    public void connect() throws IOException {
        socket = new Socket(host, port);

        // Keep same stream creation order used on server side
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());

        clientSessionState.connect();
        clientHandler = new SocketClientHandler(socket, inputStream, outputStream, serverVisitor, clientSessionState, clientView);
        clientHandlerThread = new Thread(clientHandler, "socket-client-handler");
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
