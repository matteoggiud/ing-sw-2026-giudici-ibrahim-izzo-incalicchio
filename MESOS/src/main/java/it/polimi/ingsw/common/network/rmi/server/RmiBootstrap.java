package it.polimi.ingsw.common.network.rmi.server;

import it.polimi.ingsw.common.network.rmi.client.RemoteServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiBootstrap extends Remote {
    RemoteServer connect(RemoteClientView clientView) throws RemoteException;
}
