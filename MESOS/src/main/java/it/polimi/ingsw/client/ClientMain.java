package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.socket.ClientSessionState;
import it.polimi.ingsw.client.network.socket.SocketClient;
import it.polimi.ingsw.client.network.socket.SocketVirtualServer;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.client.network.socket.messages.ServerVisitor;
import it.polimi.ingsw.client.view.tui.TuiClientApp;
import it.polimi.ingsw.client.view.tui.TuiClientView;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 12345;

        // 1. Create the View
        TuiClientView view = new TuiClientView(new Scanner(System.in));

        // 2. Create the Session State
        ClientSessionState sessionState = new ClientSessionState(null, null);

        // 3. Create the Message Visitor (it only needs the View now, not the Controller)
        ServerMessageVisitor visitor = new ServerVisitor(sessionState, view);

        // 4. Create the Socket Client
        SocketClient socketClient = new SocketClient(host, port, visitor);

        // 5. Create the Virtual Server
        SocketVirtualServer network = new SocketVirtualServer(socketClient);

        // 6. Create the Controller
        ClientController controller = new ClientController(view, network);

        // 7. Start the connection
        try {
            socketClient.connect();
        } catch (Exception e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
            return;
        }

        // 8. Start the Application
        TuiClientApp app = new TuiClientApp(controller);
        app.run();
    }
}
