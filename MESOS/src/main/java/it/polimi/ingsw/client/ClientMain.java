package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.rmi.RemoteServer;
import it.polimi.ingsw.client.network.rmi.RmiClient;
import it.polimi.ingsw.client.network.rmi.RmiVirtualServer;
import it.polimi.ingsw.client.network.socket.ClientState;
import it.polimi.ingsw.client.network.socket.SocketServerConnection;
import it.polimi.ingsw.client.network.socket.SocketVirtualServer;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.client.network.socket.messages.ServerVisitor;
import it.polimi.ingsw.client.view.tui.TuiClientApp;
import it.polimi.ingsw.client.view.tui.TuiClientView;
import it.polimi.ingsw.server.network.rmi.RemoteClientView;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        final String defaultHost = "localhost";
        final int defaultPort = 12345;
        final int socketPort = 12345;
        final int rmiPort = 1234;
        final String defaultRmiName = "MESOS_RMI";

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
        ClientState sessionState = new ClientState(null, null);
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
        TuiClientView view = new TuiClientView(sessionState);

        ViewController viewController = new ViewController(sessionState, view);

        GenericVirtualServer network;

        if(port == socketPort){
            // 3. Create the Message Visitor (it only needs the View now, not the Controller)
            ServerMessageVisitor visitor = new ServerVisitor(viewController);

            // Create the Socket Client
            SocketServerConnection socketServerConnection = new SocketServerConnection(host, port, visitor, sessionState, view);

            // Create the Virtual Server
            SocketVirtualServer socketNetwork = new SocketVirtualServer(socketServerConnection);

            // Start the connection
            try {
                socketServerConnection.connect();
            } catch (Exception e) {
                System.err.println("Could not connect to the server: " + e.getMessage());
                return;
            }

            network = socketNetwork;
        }

        else if(port == rmiPort){
            try {
                RmiClient rmiClient = new RmiClient(viewController);
                RemoteClientView clientStub =
                        (RemoteClientView) UnicastRemoteObject.exportObject(rmiClient, 0);

                Registry registry = LocateRegistry.getRegistry(host, port);
                RmiBootstrap bootstrap = (RmiBootstrap) registry.lookup(defaultRmiName);
                RemoteServer remoteServer = bootstrap.connect(clientStub);

                network = new RmiVirtualServer(remoteServer);
                sessionState.connect();
            } catch (Exception e) {
                System.err.println("Could not connect to the server (RMI): " + e.getMessage());
                return;
            }
        } else {
            System.err.println("Invalid port specified. Please use " + socketPort + " for Socket or " + rmiPort + " for RMI.");
            return;
        }


        ClientController controller = new ClientController(network);
        // Start the Application
        TuiClientApp app = new TuiClientApp(controller, sessionState, scanner, view);
        // 8. Start the Application
        app.run();
    }
}
