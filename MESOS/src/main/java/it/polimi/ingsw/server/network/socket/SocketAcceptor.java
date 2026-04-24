package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.server.controller.GameController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketAcceptor {
    private ServerSocket listenSocket;
    private GameController controller;

    List<ClientHandler> clients = new ArrayList<ClientHandler>();

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


            ClientHandler client = new ClientHandler(clientSocket, controller,  inputStream, outputStream);
            clients.add(client);
            new Thread(client).start();
        }
    }
}
