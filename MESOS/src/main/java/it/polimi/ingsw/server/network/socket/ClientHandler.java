package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.ClientSession;
import it.polimi.ingsw.server.network.SessionRegistry;
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
    private final SocketVirtualView socketVirtualView;
    private final ClientVisitor clientVisitor;
    private final SocketAcceptor socketAcceptor;


    public ClientHandler(Socket clientSocket, GameController controller, ObjectInputStream inputStream, ObjectOutputStream outputStream, SocketAcceptor socketAcceptor) {
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.socketVirtualView = new SocketVirtualView(outputStream);
        this.clientVisitor = new ClientVisitor(controller, new ClientSession(clientSocket, this.socketVirtualView));
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

    private void sendWelcome() {
        LoginNeededMessage welcomeMessage = new LoginNeededMessage("Welcome! Please log in or register to continue.");
        socketVirtualView.sendMessage(welcomeMessage);
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
        SessionRegistry.getInstance().unregisterSession(clientVisitor.getClientSession());
    }


}
