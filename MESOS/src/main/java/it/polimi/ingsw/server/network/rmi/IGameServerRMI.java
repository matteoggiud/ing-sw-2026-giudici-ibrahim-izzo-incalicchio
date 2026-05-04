package it.polimi.ingsw.server.network.rmi;

//Methods that the client can invoke

import it.polimi.ingsw.server.network.ClientSession;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameServerRMI extends Remote {
    void login(ClientSession clientSession, String nickname, String password) throws RemoteException;
}
