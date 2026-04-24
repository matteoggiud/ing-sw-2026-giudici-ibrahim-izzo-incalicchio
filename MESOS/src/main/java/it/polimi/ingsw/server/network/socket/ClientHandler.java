package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.server.controller.GameController;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final GameController controller;


     public ClientHandler(Socket clientSocket,  GameController controller, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.controller = controller;
    }

    @Override
    public void run() {

    }
}
