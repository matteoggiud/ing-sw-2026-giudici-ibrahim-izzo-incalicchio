package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.rmi.RemoteServer;
import it.polimi.ingsw.client.network.rmi.RmiClient;
import it.polimi.ingsw.client.network.rmi.RmiVirtualServer;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.network.socket.SocketServerConnection;
import it.polimi.ingsw.client.network.socket.SocketVirtualServer;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.client.network.socket.messages.ServerVisitor;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.gui.GuiClientView;
import it.polimi.ingsw.client.view.gui.GuiContext;
import it.polimi.ingsw.client.view.tui.TuiClientApp;
import it.polimi.ingsw.server.network.rmi.RemoteClientView;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NetworkBuilder {
    private static final int SOCKET_PORT = 12345;

    private static final int RMI_PORT = 1234;
    private static final String RMI_NAME = "MESOS_RMI";

    private final String host;
    private final ViewController viewController;

    public NetworkBuilder(String host, ViewController viewController) {
        this.host = host;
        this.viewController = viewController;
    }


    public void createSocketConnection(ClientState clientState, GuiClientView view) {
        ServerMessageVisitor visitor = new ServerVisitor(viewController);
        SocketServerConnection socketServerConnection = new SocketServerConnection(host, SOCKET_PORT, visitor, clientState, view);
        GenericVirtualServer network = new SocketVirtualServer(socketServerConnection);
        ClientController clientController = new ClientController(network);
        view.setGuiContext(new GuiContext(clientController, clientState));

        try {
            socketServerConnection.connect();
        } catch (Exception e) {
            viewController.onGenericError("Failed to connect to server: " + e.getMessage());
        }

    }

    public void createRmiConnection(ClientState clientState, GuiClientView view) {
        try {
            // Set the RMI server hostname to the local IP address to ensure callbacks work correctly
            System.setProperty("java.rmi.server.hostname", InetAddressLookup.getLocalHostAddress());

            RmiClient rmiClient = new RmiClient(viewController);

            RemoteClientView clientStub = (RemoteClientView) UnicastRemoteObject.exportObject(rmiClient, 0);

            Registry registry = LocateRegistry.getRegistry(host, RMI_PORT);
            RmiBootstrap bootstrap = (RmiBootstrap) registry.lookup(RMI_NAME);
            RemoteServer remoteServer = bootstrap.connect(clientStub);

            GenericVirtualServer network = new RmiVirtualServer(remoteServer);
            ClientController clientController = new ClientController(network);
            view.setGuiContext(new GuiContext(clientController, clientState));
        } catch (Exception e) {
            viewController.onGenericError("Failed to connect to server: " + e.getMessage());
        }
    }

    public void createSocketConnection(ClientState clientState, ClientView view,TuiClientApp tuiClientApp) {
        ServerMessageVisitor visitor = new ServerVisitor(viewController);
        SocketServerConnection socketServerConnection = new SocketServerConnection(host, SOCKET_PORT, visitor, clientState, view);
        GenericVirtualServer network = new SocketVirtualServer(socketServerConnection);

        ClientController clientController = new ClientController(network);
        tuiClientApp.setController(clientController);

        try {
            socketServerConnection.connect();
        } catch (Exception e) {
            viewController.onGenericError("Failed to connect to server: " + e.getMessage());
        }

    }


    public void createRmiConnection(TuiClientApp clientApp) {
        try {
            // Set the RMI server hostname to the local IP address to ensure callbacks work correctly
            System.setProperty("java.rmi.server.hostname", InetAddressLookup.getLocalHostAddress());

            RmiClient rmiClient = new RmiClient(viewController);

            RemoteClientView clientStub = (RemoteClientView) UnicastRemoteObject.exportObject(rmiClient, 0);

            Registry registry = LocateRegistry.getRegistry(host, RMI_PORT);
            RmiBootstrap bootstrap = (RmiBootstrap) registry.lookup(RMI_NAME);
            RemoteServer remoteServer = bootstrap.connect(clientStub);

            GenericVirtualServer network = new RmiVirtualServer(remoteServer);
            ClientController clientController = new ClientController(network);
            clientApp.setController(clientController);
        } catch (Exception e) {
            viewController.onGenericError("Failed to connect to server: " + e.getMessage());
        }
    }
}
