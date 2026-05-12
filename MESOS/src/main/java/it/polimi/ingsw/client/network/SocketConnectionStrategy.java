package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.socket.SocketServerConnection;
import it.polimi.ingsw.client.network.socket.SocketVirtualServer;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.client.network.socket.messages.ServerVisitor;
import it.polimi.ingsw.client.view.ClientView;

public class SocketConnectionStrategy implements NetworkConnectionStrategy {
    private static final int SOCKET_PORT = 12345;

    @Override
    public GenericVirtualServer connect(
            String host,
            ViewController viewController,
            ClientState clientState,
            ClientView view
    ) throws Exception {
        ServerMessageVisitor visitor = new ServerVisitor(viewController);
        SocketServerConnection connection =
                new SocketServerConnection(host, SOCKET_PORT, visitor, clientState, view);

        connection.connect();
        return new SocketVirtualServer(connection);
    }
}
