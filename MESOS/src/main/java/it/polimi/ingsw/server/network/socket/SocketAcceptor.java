package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.EventHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

/**
 * Class that accepts incoming socket connections and creates a ClientHandler for each of them.
 */
public class SocketAcceptor {
    private final ServerSocket listenSocket;
    private final GameController controller;

    List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public SocketAcceptor(ServerSocket listenSocket, GameController controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
    }

    public void run() throws IOException {
        Socket clientSocket;

        while ((clientSocket = this.listenSocket.accept()) != null) {

            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            outputStream.flush();

            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());


            ClientHandler client = new ClientHandler(clientSocket, controller, inputStream, outputStream, this, new EventHandler(new SocketGenericVirtualView(clientSocket, outputStream)));
            clients.add(client);
            new Thread(client).start();
        }
    }


    public void removeClientHandler(ClientHandler clientHandler) {
        this.clients.remove(clientHandler);
    }
}
