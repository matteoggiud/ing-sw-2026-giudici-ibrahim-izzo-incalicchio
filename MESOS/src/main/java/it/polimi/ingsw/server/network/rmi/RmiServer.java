package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.RemoteServer;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.network.ClientSession;

import java.rmi.RemoteException;

public class RmiServer implements RemoteServer {

    private final GameController gameController;
    private final ClientSession clientSession;

    public RmiServer(GameController gameController, ClientSession clientSession) {
        this.gameController = gameController;
        this.clientSession = clientSession;
    }

    @Override
    public void loginRequest(String username, String password) throws RemoteException {
    }

    @Override
    public void onRegisterMessage(String username, String password) throws RemoteException {

    }

    @Override
    public void onCreateGameMessage(int numPlayers) throws RemoteException {

    }

    @Override
    public void onJoinGameMessage(int gameId) throws RemoteException {

    }

    @Override
    public void onGetAvailableGamesMessage() throws RemoteException {

    }

    @Override
    public void onDisconnectMessage() throws RemoteException {

    }

    @Override
    public void onPlaceTotemMessage(int offerBoxPosition) throws RemoteException {

    }

    @Override
    public void onLeftGameMessage() throws RemoteException {

    }

    @Override
    public void onDrawCardMessage(int cardId, boolean fromUpperRow) throws RemoteException {

    }

    @Override
    public void onDrawExtraCardMessage(int cardId) throws RemoteException {

    }

    @Override
    public void onGetRankings(int numPlayers) throws RemoteException {

    }
}
