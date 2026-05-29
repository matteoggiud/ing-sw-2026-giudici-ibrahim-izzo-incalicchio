package it.polimi.ingsw.common.network.rmi.client;

import java.rmi.Remote;

public interface RemoteServer extends Remote {

     void loginRequest(String username, String password) throws java.rmi.RemoteException;
     void onRegisterMessage(String username, String password) throws java.rmi.RemoteException;
     void onCreateGameMessage(int numPlayers) throws java.rmi.RemoteException;
     void onJoinGameMessage(int gameId) throws java.rmi.RemoteException;
     void onGetAvailableGamesMessage() throws java.rmi.RemoteException;
     void onDisconnectMessage() throws java.rmi.RemoteException;
     void onPlaceTotemMessage(int offerBoxPosition) throws java.rmi.RemoteException;
     void onLeftGameMessage() throws java.rmi.RemoteException;
     void onDrawCardMessage(int cardId, boolean fromUpperRow) throws java.rmi.RemoteException;
     void onDrawExtraCardMessage(int cardId) throws java.rmi.RemoteException;
     void onGetRankings(int numPlayers) throws java.rmi.RemoteException;
     void onLeftLobbyMessage() throws java.rmi.RemoteException;
}
