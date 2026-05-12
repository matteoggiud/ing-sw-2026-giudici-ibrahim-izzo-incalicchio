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
    private final String host;
    private final ViewController viewController;

    public NetworkBuilder(String host, ViewController viewController) {
        this.host = host;
        this.viewController = viewController;
    }

    public ClientController createConnection(
            NetworkConnectionStrategy strategy,
            ClientState clientState,
            ClientView view
    ) {
        try {
            GenericVirtualServer network =
                    strategy.connect(host, viewController, clientState, view);

            return new ClientController(network);
        } catch (Exception e) {
            viewController.onGenericError("Failed to connect to server: " + e.getMessage());
            return null;
        }
    }
}

