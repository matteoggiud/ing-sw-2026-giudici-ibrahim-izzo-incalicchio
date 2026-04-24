package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.socket.messages.ClientVisitor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {


    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ClientVisitor clientVisitor;


     public ClientHandler(Socket clientSocket,  GameController controller, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.clientVisitor = new ClientVisitor(controller);


    }

    @Override
    public void run() {
        while (true) {
            try {
                ClientMessage message = (ClientMessage) inputStream.readObject();
                message.dispatch(clientVisitor);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }


}
