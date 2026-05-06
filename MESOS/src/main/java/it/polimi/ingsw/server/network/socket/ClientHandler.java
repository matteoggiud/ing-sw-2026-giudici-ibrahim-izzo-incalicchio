package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.ClientSession;
import it.polimi.ingsw.server.network.EventHandler;
import it.polimi.ingsw.server.network.GenericVirtualView;
import it.polimi.ingsw.server.network.socket.messages.ClientVisitor;
import it.polimi.ingsw.server.network.socket.messages.login.LoginNeededMessage;

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
    private final EventHandler eventHandler;
    private final ClientVisitor clientVisitor;
    private final SocketAcceptor socketAcceptor;


    public ClientHandler(Socket clientSocket, GameController controller, ObjectInputStream inputStream, ObjectOutputStream outputStream, SocketAcceptor socketAcceptor, EventHandler eventHandler) {
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.eventHandler = eventHandler;
        this.clientVisitor = new ClientVisitor(controller, new ClientSession(clientSocket, eventHandler));
        this.socketAcceptor = socketAcceptor;
    }

    @Override
    public void run() {
        try {
            sendWelcome();

            while (!clientSocket.isClosed()) {
                // Read a message from the client
                ClientMessage message = (ClientMessage) inputStream.readObject();

                // Dispatch the message to the client handler in the server
                message.dispatch(clientVisitor);
            }
        } catch (EOFException | SocketException e) {
            //Normal disconnection
        } catch (Exception e) {
            //Unexpected error during communication with the client
            e.printStackTrace();
        } finally {
            clientVisitor.getGameController().handleDisconnect(clientVisitor.getClientSession());

            safeClose();

            socketAcceptor.removeClientHandler(this);
        }
    }

    //TODO: fix it!! :/
    private void sendWelcome() {
        eventHandler.getView().onAuthenticationNeeded("Welcome! Please log in or register to continue.");
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
