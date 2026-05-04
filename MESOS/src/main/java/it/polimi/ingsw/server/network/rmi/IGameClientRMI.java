package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGameClientRMI extends Remote {
    void onLoginSuccess(String nickname, List<GameInfoDTO> availableGames) throws RemoteException;
}
