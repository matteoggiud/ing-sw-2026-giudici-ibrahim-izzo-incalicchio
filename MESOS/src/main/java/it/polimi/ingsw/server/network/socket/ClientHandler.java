package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.socket.messages.ClientVisitor;

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
    private final SocketVirtualView socketVirtualView;
    private final ClientVisitor clientVisitor;


    public ClientHandler(Socket clientSocket, GameController controller, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.socketVirtualView = new SocketVirtualView(outputStream);
        this.clientVisitor = new ClientVisitor(controller, new ClientSession(clientSocket, this.socketVirtualView));
    }

    @Override
    public void run() {
        try {

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

        }
    }


}
