package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.rmi.RemoteServer;
import it.polimi.ingsw.client.network.rmi.RmiClient;
import it.polimi.ingsw.client.network.rmi.RmiVirtualServer;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.server.network.rmi.RemoteClientView;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiConnectionStrategy implements NetworkConnectionStrategy {
    private static final int RMI_PORT = 1234;
    private static final String RMI_NAME = "MESOS_RMI";

    @Override
    public GenericVirtualServer connect(
            String host,
            ViewController viewController,
            ClientState clientState,
            ClientView view
    ) throws Exception {
        System.setProperty("java.rmi.server.hostname", InetAddressLookup.getLocalHostAddress());

        RmiClient rmiClient = new RmiClient(viewController);
        RemoteClientView clientStub = (RemoteClientView) UnicastRemoteObject.exportObject(rmiClient, 0);

        Registry registry = LocateRegistry.getRegistry(host, RMI_PORT);
        RmiBootstrap bootstrap = (RmiBootstrap) registry.lookup(RMI_NAME);
        RemoteServer remoteServer = bootstrap.connect(clientStub);

        return new RmiVirtualServer(remoteServer,  rmiClient);
    }
}

