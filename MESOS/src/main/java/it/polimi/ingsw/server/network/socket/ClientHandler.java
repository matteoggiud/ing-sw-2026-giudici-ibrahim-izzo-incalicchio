package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.ClientSession;
import it.polimi.ingsw.server.network.EventHandler;
import it.polimi.ingsw.common.network.socket_messages.visitors.ClientVisitor;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Handles communication with a single client in a separate thread.
 */
public class ClientHandler implements Runnable {


    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ClientVisitor clientVisitor;
    private final SocketAcceptor socketAcceptor;


    public ClientHandler(Socket clientSocket, GameController controller, ObjectInputStream inputStream, ObjectOutputStream outputStream, SocketAcceptor socketAcceptor, EventHandler eventHandler) {
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.clientVisitor = new ClientVisitor(controller, new ClientSession(eventHandler));
        this.socketAcceptor = socketAcceptor;
    }

    @Override
    public void run() {
        try {

            while (!clientSocket.isClosed() && !clientVisitor.getClientSession().isRequestedDisconnection()) {
                // Read a message from the client
                ClientMessage message = (ClientMessage) inputStream.readObject();

                // Dispatch the message to the client handler in the server
                message.dispatch(clientVisitor);
            }
        } catch (EOFException | SocketException e) {
            //Normal disconnection
            System.out.println("Client disconnected: " + e.getMessage());
        } catch (Exception e) {
            //Unexpected error during communication with the client
            System.out.println("Error in communication with client: " + e.getMessage());
        } finally {
            safeClose();
            if (!clientVisitor.getClientSession().isRequestedDisconnection()) {
                clientVisitor.getGameController().handleUnexpectedDisconnect(clientVisitor.getClientSession());
            }
            socketAcceptor.removeClientHandler(this);
        }
    }


    private void safeClose() {
        try {
            inputStream.close();
        } catch (Exception ignored) {
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        try {
            clientSocket.close();
        } catch (Exception ignored) {
        }
    }


}
