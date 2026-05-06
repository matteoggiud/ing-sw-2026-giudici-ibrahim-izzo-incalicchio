package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.network.socket.VirtualServer;
import it.polimi.ingsw.client.network.socket.messages.game.*;
import it.polimi.ingsw.client.network.socket.messages.login.LoginSubmitMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.DisconnectMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetAvailableGamesMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetRankingsMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.LeftGameMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterSubmitMessage;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.Credentials;

public class ClientController {
    private final ClientView view;
    private final GenericVirtualServer virtualServer;


    public ClientController(ClientView view, GenericVirtualServer virtualServer) {
        this.view = view;
        this.virtualServer = virtualServer;
    }

    public void login() {
        Credentials credentials = view.requestLoginCredentials();
        virtualServer.onLoginMessage(credentials.nickname(), credentials.passwordHash());
    }

    public void register() {
        Credentials credentials = view.requestRegisterCredentials();
        virtualServer.onRegisterMessage(credentials.nickname(), credentials.passwordHash());
    }

    public void showError(String message) {
        view.showError(message);
    }

    public void showMessage(String message) {
        view.showMessage(message);
    }

    public void createGame(int expectedNumOfPlayers) {
        virtualServer.onCreateGameMessage(expectedNumOfPlayers);
    }

    public void joinGame(int gameId) {
        virtualServer.onJoinGameMessage(gameId);
    }

    public void getAvailableGames() {
        virtualServer.onGetAvailableGamesMessage();
    }

    public void disconnect() {
        virtualServer.onDisconnectMessage();
    }

    public void placeTotem(int offerBoxPosition) {
        virtualServer.onPlaceTotemMessage(offerBoxPosition);
    }

    public void leftGame() {
        virtualServer.onLeftGameMessage();
    }

    public void drawCard(int selectedCardId, boolean fromUpperRow) {
        virtualServer.onDrawCardMessage(selectedCardId, fromUpperRow);
    }

    public void drawExtraCard(int selectedCardId) {
        virtualServer.onDrawExtraCardMessage(selectedCardId);
    }

    public void getRankings(int numPlayers){
        virtualServer.onGetRankings(numPlayers);
    }
}
