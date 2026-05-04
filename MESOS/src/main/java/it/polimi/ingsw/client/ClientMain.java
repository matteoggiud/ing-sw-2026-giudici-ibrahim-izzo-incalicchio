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
        final String defaultHost = "localhost";
        final int defaultPort = 12345;

        // Shared Scanner for user input (passed to view and app)
        Scanner scanner = new Scanner(System.in);

        // Parse host and port from command-line arguments or prompt the user
        String host;
        int port;

        if (args.length >= 1) {
            host = args[0];
        } else {
            System.out.print("Server host [" + defaultHost + "]: ");
            String line = scanner.nextLine().trim();
            host = line.isEmpty() ? defaultHost : line;
        }

        // 1. Create the Session State
        ClientSessionState sessionState = new ClientSessionState(null, null);
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Port argument not a number, using default " + defaultPort);
                port = defaultPort;
            }
        } else {
            System.out.print("Server port [" + defaultPort + "]: ");
            String portLine = scanner.nextLine().trim();
            if (portLine.isEmpty()) {
                port = defaultPort;
            } else {
                try {
                    port = Integer.parseInt(portLine);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port input, using default " + defaultPort);
                    port = defaultPort;
                }
            }
        }


        // 2. Create the View
        TuiClientView view = new TuiClientView(scanner, sessionState);


        // 3. Create the Message Visitor (it only needs the View now, not the Controller)
        ServerMessageVisitor visitor = new ServerVisitor(sessionState, view);

        // Create the Socket Client
        SocketClient socketClient = new SocketClient(host, port, visitor, sessionState, view);

        // Create the Virtual Server
        SocketVirtualServer network = new SocketVirtualServer(socketClient);

        // Create the Controller
        ClientController controller = new ClientController(view, network);

        // Start the connection
        try {
            socketClient.connect();
        } catch (Exception e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
            return;
        }

        // Start the Application
        TuiClientApp app = new TuiClientApp(controller, sessionState, scanner, view);
        // 8. Start the Application
        app.run();
    }
}
