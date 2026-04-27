package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class SocketClientHandler implements Runnable {
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ServerMessageVisitor serverVisitor;

    public SocketClientHandler(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream, ServerMessageVisitor serverVisitor) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.serverVisitor = serverVisitor;
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
            while (!socket.isClosed()) {
                ServerMessage serverMessage = (ServerMessage) inputStream.readObject();
                serverMessage.dispatch(serverVisitor);
            }
        } catch (EOFException | SocketException e) {
            // Normal disconnection from server
        } catch (Exception e) {
            // Unexpected communication error
            e.printStackTrace();
        } finally {
            safeClose();
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
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
