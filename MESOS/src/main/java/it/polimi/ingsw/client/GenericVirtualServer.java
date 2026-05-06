package it.polimi.ingsw.client;

import java.rmi.Remote;

public interface GenericVirtualServer {

    void onLoginMessage(String username, String password);
    void onLogoutMessage(String username, String password);
    void onRegisterMessage(String username, String password);
    void showErrorMessage(String message);
    void showMessage(String message);
    void onCreateGameMessage(int numPlayers);
    void onJoinGameMessage(int gameId);
    void onGetAvailableGamesMessage();
    void onDisconnectMessage();
    void onPlaceTotemMessage(int offerBoxPosition);
    void onLeftGameMessage();
    void onDrawCardMessage(int cardId, boolean fromUpperRow);
    void onDrawExtraCardMessage(int cardId);
    void onGetRankings(int numPlayers);
}
