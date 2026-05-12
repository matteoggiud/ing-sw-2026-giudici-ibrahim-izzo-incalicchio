package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.view.ClientView;


/**
 * Strategy interface for network connection. This allows the client to choose between different network protocols (e.g., Socket, RMI)
 * at runtime without changing the client code.
 */
public interface NetworkConnectionStrategy {
    GenericVirtualServer connect(
            String host,
            ViewController viewController,
            ClientState clientState,
            ClientView view
    ) throws Exception;
}




