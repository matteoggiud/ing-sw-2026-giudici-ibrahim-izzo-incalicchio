package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class SocketServerListener implements Runnable {
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ServerMessageVisitor serverVisitor;

    private final ClientState clientSession;
    private final ClientView clientView;

    public SocketServerListener(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream, ServerMessageVisitor serverVisitor, ClientState clientSession, ClientView clientView) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.serverVisitor = serverVisitor;
        this.clientSession = clientSession;
        this.clientView = clientView;
    }


    public void sendMessage(ClientMessage message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        try {
            while (!socket.isClosed() && clientSession.isConnected()) {
                ServerMessage serverMessage = (ServerMessage) inputStream.readObject();
                serverMessage.dispatch(serverVisitor);
            }
        } catch (EOFException | SocketException e) {
            handleServerDisconnected("Connection lost: server disconnected.");
        } catch (Exception e) {
            handleServerDisconnected("Connection lost: unexpected communication error.");
        } finally {
            safeClose();
            clientView.shutdown("Connection lost");
        }
    }

    /**
     * Closes socket and streams safely.
     */
    public void safeClose() {
        try {
            inputStream.close();
        } catch (Exception ignored) {
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        try {
            socket.close();
        } catch (Exception ignored) {
        }
        clientSession.disconnect();
    }

    private void handleServerDisconnected(String message) {
        if (clientSession.isConnected()) {
            clientSession.disconnect();
            clientSession.setErrorMessage(message);
            clientView.showError(message);
        }
    }

}
