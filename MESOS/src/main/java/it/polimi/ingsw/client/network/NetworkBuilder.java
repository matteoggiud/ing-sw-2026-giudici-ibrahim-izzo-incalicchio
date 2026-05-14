package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.view.ClientView;

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

