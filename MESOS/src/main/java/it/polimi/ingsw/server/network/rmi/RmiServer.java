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
        gameController.login(clientSession, username, password);
    }

    @Override
    public void onRegisterMessage(String username, String password) throws RemoteException {
        gameController.register(clientSession, username, password);
    }

    @Override
    public void onCreateGameMessage(int numPlayers) throws RemoteException {
        gameController.createGame(clientSession, numPlayers);
    }

    @Override
    public void onJoinGameMessage(int gameId) throws RemoteException {
        gameController.joinGame(clientSession, gameId);
    }

    @Override
    public void onGetAvailableGamesMessage() throws RemoteException {
        gameController.getAvailableGames(clientSession);
    }

    @Override
    public void onDisconnectMessage() throws RemoteException {
        gameController.handleDisconnect(clientSession);
    }

    @Override
    public void onPlaceTotemMessage(int offerBoxPosition) throws RemoteException {
        gameController.placing(clientSession, offerBoxPosition);
    }

    @Override
    public void onLeftGameMessage() throws RemoteException {
        gameController.leaveGame(clientSession);
    }

    @Override
    public void onDrawCardMessage(int cardId, boolean fromUpperRow) throws RemoteException {
        gameController.drawCard(clientSession, cardId, fromUpperRow);
    }

    @Override
    public void onDrawExtraCardMessage(int cardId) throws RemoteException {
        gameController.drawCard(clientSession, cardId, true);
    }

    @Override
    public void onGetRankings(int numPlayers) throws RemoteException {
        gameController.getRankings(clientSession, numPlayers);
    }
}
