package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.view.Credentials;

public class ClientController {
    private final GenericVirtualServer virtualServer;


    public ClientController(GenericVirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public void submitLogin(Credentials credentials) {
        virtualServer.onLoginRequest(credentials.nickname(), credentials.passwordHash());
    }

    public void submitRegistration(Credentials credentials) {
        virtualServer.onRegisterMessage(credentials.nickname(), credentials.passwordHash());
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
