package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.InetAddressLookup;
import it.polimi.ingsw.client.network.rmi.RemoteServer;
import it.polimi.ingsw.client.network.rmi.RmiClient;
import it.polimi.ingsw.client.network.rmi.RmiVirtualServer;
import it.polimi.ingsw.client.network.socket.ClientState;
import it.polimi.ingsw.client.network.socket.SocketServerConnection;
import it.polimi.ingsw.client.network.socket.SocketVirtualServer;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.client.network.socket.messages.ServerVisitor;

import it.polimi.ingsw.client.view.gui.GuiClientApp;
import it.polimi.ingsw.client.view.tui.TuiClientApp;
import it.polimi.ingsw.client.view.tui.TuiClientView;
import it.polimi.ingsw.server.network.rmi.RemoteClientView;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;
import javafx.application.Application;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
        final int rmiClientCallbackPort = 1237;
        final String defaultRmiName = "MESOS_RMI";

        String defaultClientIp = InetAddressLookup.getLocalHostAddress();

        // Shared Scanner for user input (passed to view and app)
        Scanner scanner = new Scanner(System.in);

        // Parse host and port from command-line arguments or prompt the user
        String clientIp;

        System.out.println("--- MESOS CLIENT ---");

        // Step 1: Choose UI
        System.out.println("1) GUI client");
        System.out.println("2) TUI client");
        System.out.print("Select interface [1]: ");
        String uiChoice = scanner.nextLine().trim();
        // Default to GUI if input is invalid
        boolean useGui = !uiChoice.equals("2");

        // Step 2: Choose protocol
        System.out.println("\n1) Socket");
        System.out.println("2) RMI");
        System.out.print("Select protocol [1]: ");
        String protocolChoice = scanner.nextLine().trim();
        // Default to Socket if input is invalid
        Integer port = protocolChoice.equals("2") ? rmiPort : socketPort;

        System.out.print("\nServer ip [localhost]: ");
        String host = scanner.nextLine().trim();
        if (host.isEmpty()) host = "localhost";

        String[] appArgs = {host, port.toString()};

        if (useGui) {
            Application.launch(GuiClientApp.class, appArgs);
        } else {
            TuiClientApp tuiApp = new TuiClientApp(scanner);
            tuiApp.init(appArgs);
            tuiApp.run();
        }
        }
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

        if (args.length >= 3 && !args[2].isBlank()) {
            clientIp = args[2];
        } else {
            System.out.print("Client ip [" + defaultClientIp + "]: ");
            String clientIpLine = scanner.nextLine().trim();
            if (clientIpLine.isEmpty()) {
                clientIp = defaultClientIp;
            } else {
                clientIp = clientIpLine;
            }
        }

        System.setProperty("java.rmi.server.hostname", clientIp);

        // 2. Create the View
        TuiClientView view = new TuiClientView(sessionState);

        ViewController viewController = new ViewController(sessionState, view);

        GenericVirtualServer network;

        if (port == socketPort) {
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
        } else if (port == rmiPort) {
            try {
                RmiClient rmiClient = new RmiClient(viewController);

                //The Remote version of the client that the server can call, the server use the clientStud to callback to the client
                //We create a remoteclient given to the server and every client listen the server on the port 1237, which is our remote
                //object
                RemoteClientView clientStub =
                        (RemoteClientView) UnicastRemoteObject.exportObject(rmiClient, rmiClientCallbackPort);

                //Just like a map it associates a logic name to a remote object
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
