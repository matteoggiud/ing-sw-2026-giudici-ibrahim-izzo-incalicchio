package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.RemoteServer;
import it.polimi.ingsw.server.network.CommandHandler;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.ClientSession;

import java.rmi.RemoteException;

public class RmiServer implements RemoteServer {

    private final GameController gameController;
    private final ClientSession clientSession;
    private final CommandHandler commandHandler;

    public RmiServer(GameController gameController, ClientSession clientSession) {
        this.gameController = gameController;
        this.clientSession = clientSession;
        this.commandHandler = new CommandHandler(gameController, clientSession);
    }

    @Override
    public void loginRequest(String username, String password) throws RemoteException {
        commandHandler.handleLogin(username, password);
    }

    @Override
    public void onRegisterMessage(String username, String password) throws RemoteException {
        commandHandler.handleRegistration(username, password);
    }

    @Override
    public void onCreateGameMessage(int numPlayers) throws RemoteException {
        commandHandler.handleGameCreation(numPlayers);
    }

    @Override
    public void onJoinGameMessage(int gameId) throws RemoteException {
        commandHandler.handleJoinGame(gameId);
    }

    @Override
    public void onGetAvailableGamesMessage() throws RemoteException {
        commandHandler.handleGetAvailableGames();
    }

    @Override
    public void onDisconnectMessage() throws RemoteException {
        commandHandler.handleDisconnection();
    }

    @Override
    public void onPlaceTotemMessage(int offerBoxPosition) throws RemoteException {
        commandHandler.handlePlaceTotem(offerBoxPosition);
    }

    @Override
    public void onLeftGameMessage() throws RemoteException {
        commandHandler.handleLeftGame();
    }

    @Override
    public void onDrawCardMessage(int cardId, boolean fromUpperRow) throws RemoteException {
        commandHandler.handleDrawCard(cardId, fromUpperRow);
    }

    @Override
    public void onDrawExtraCardMessage(int cardId) throws RemoteException {
        commandHandler.handleExtraDraw(cardId);
    }

    @Override
    public void onGetRankings(int numPlayers) throws RemoteException {
        commandHandler.handleRankings(numPlayers);
    }

    @Override
    public void onLeftLobbyMessage() throws RemoteException {
        commandHandler.handleLeftLobby();
    }
}
