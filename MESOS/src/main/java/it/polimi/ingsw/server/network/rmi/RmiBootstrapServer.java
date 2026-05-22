package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.network.rmi.RemoteServer;
import it.polimi.ingsw.client.network.rmi.RmiVirtualServer;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.ClientSession;
import it.polimi.ingsw.server.network.EventHandler;
import it.polimi.ingsw.server.network.GenericVirtualView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RmiBootstrapServer implements RmiBootstrap {

    private final GameController gameController;
    private final List<RmiServer> activeServers = new CopyOnWriteArrayList<>(); //TODO: remove unused objects
    final int serverPort = 0;

    public RmiBootstrapServer(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public RemoteServer connect(RemoteClientView clientView) throws RemoteException {
        GenericVirtualView virtualView = new RmiGenericVirtualView(clientView);

        //TODO: Review this part when possible
        ClientSession session = new ClientSession(new EventHandler(virtualView));

        RmiServer server = new RmiServer(gameController, session);
        activeServers.add(server);

        return (RemoteServer) UnicastRemoteObject.exportObject(server, serverPort);
    }
}
