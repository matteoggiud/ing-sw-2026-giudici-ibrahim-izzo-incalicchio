package it.polimi.ingsw.server.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteClientView extends Remote {

    void onGameStarted(int gameId, List<String> playerNicknames) throws RemoteException;
}
