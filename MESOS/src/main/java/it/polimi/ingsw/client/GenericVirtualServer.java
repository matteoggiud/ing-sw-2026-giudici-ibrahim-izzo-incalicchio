package it.polimi.ingsw.client;

public interface GenericVirtualServer {

    void onLoginRequest(String username, String password);
    void onRegisterMessage(String username, String password);
    void onCreateGameMessage(int numPlayers);
    void onJoinGameMessage(int gameId);
    void onGetAvailableGamesMessage();
    void onDisconnectMessage();
    void onPlaceTotemMessage(int offerBoxPosition);
    void onLeftGameMessage();
    void onDrawCardMessage(int cardId, boolean fromUpperRow);
    void onDrawExtraCardMessage(int cardId);
    void onGetRankings(int numPlayers);
    void onLeftLobbyMessage();
}
