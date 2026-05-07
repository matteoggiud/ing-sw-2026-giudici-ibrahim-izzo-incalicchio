package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.RemoteServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiBootstrap extends Remote {
    RemoteServer connect(RemoteClientView clientView) throws RemoteException;
}
