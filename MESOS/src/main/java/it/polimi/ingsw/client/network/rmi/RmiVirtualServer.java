package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.client.GenericVirtualServer;

import it.polimi.ingsw.client.network.rmi.RemoteServer;

import java.rmi.RemoteException;

public class RmiVirtualServer implements GenericVirtualServer {

    RemoteServer remoteServer;
    private final RmiClient rmiClient;

    public RmiVirtualServer(RemoteServer remoteServer, RmiClient rmiClient) {
        this.remoteServer = remoteServer;
        this.rmiClient = rmiClient;
    }


    @Override
    public void onLoginRequest(String username, String passwordHash) {
        try {
            remoteServer.loginRequest(username,passwordHash);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onRegisterMessage(String username, String password) {
        try {
            remoteServer.onRegisterMessage(username,password);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreateGameMessage(int numPlayers) {
        try {
            remoteServer.onCreateGameMessage(numPlayers);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onJoinGameMessage(int gameId) {
        try {
            remoteServer.onJoinGameMessage(gameId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGetAvailableGamesMessage() {
        try {
            remoteServer.onGetAvailableGamesMessage();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisconnectMessage() {
        try {
            remoteServer.onDisconnectMessage();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPlaceTotemMessage(int offerBoxPosition) {
        try {
            remoteServer.onPlaceTotemMessage(offerBoxPosition);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLeftGameMessage() {
        try {
            remoteServer.onLeftGameMessage();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDrawCardMessage(int cardId, boolean fromUpperRow) {
        try {
            remoteServer.onDrawCardMessage(cardId, fromUpperRow);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDrawExtraCardMessage(int cardId) {
        try {
            remoteServer.onDrawExtraCardMessage(cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGetRankings(int numPlayers) {
        try {
            remoteServer.onGetRankings(numPlayers);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLeftLobbyMessage() {
        try {
            remoteServer.onLeftLobbyMessage();
        }catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
