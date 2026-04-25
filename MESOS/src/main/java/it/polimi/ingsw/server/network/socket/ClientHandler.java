package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.socket.messages.ClientVisitor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handles communication with a single client in a separate thread.
 */
public class ClientHandler implements Runnable {


    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final SocketVirtualView socketVirtualView;
    private final ClientVisitor clientVisitor;


    public ClientHandler(Socket clientSocket, GameController controller, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        SocketVirtualView socketVirtualView = new SocketVirtualView(outputStream);

        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.socketVirtualView = socketVirtualView;
        this.clientVisitor = new ClientVisitor(controller, new ClientSession(clientSocket, socketVirtualView));
    }

    @Override
    public void run() {
        while (!clientSocket.isClosed()) {
            try {
                // Read a message from the client
                ClientMessage message = (ClientMessage) inputStream.readObject();

                // Dispatch the message to the client handler in the server
                message.dispatch(clientVisitor);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            } finally {
                clientVisitor.getGameController().handleDisconnect(clientVisitor.getClientSession());
            }
        }
    }


}
