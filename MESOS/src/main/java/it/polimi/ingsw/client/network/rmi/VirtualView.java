package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;

import java.rmi.RemoteException;
import java.util.List;

public interface VirtualView {
    void onLoginSuccess(String nickname) throws RemoteException;

    void onLoginFailed(String reason) throws RemoteException;

    void onRegisterSuccess(String nickname) throws RemoteException;

    void onRegisterFailed(String reason) throws RemoteException;
}
