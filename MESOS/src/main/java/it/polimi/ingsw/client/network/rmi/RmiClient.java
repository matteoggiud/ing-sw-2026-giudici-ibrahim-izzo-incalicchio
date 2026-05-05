package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.server.network.rmi.RemoteClientView;

import java.rmi.RemoteException;
import java.util.List;


public class RmiClient implements RemoteClientView {
    private final ClientView clientView;

    public RmiClient(ClientView clientView) {
        this.clientView = clientView;
    }


    /**
     * Called when a game starts, providing the game ID and the list of player nicknames.
     *
     * @param gameId          the unique identifier of the started game
     * @param playerNicknames the list of nicknames of the players participating in the game
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void onGameStarted(int gameId, List<String> playerNicknames) throws RemoteException {
        clientView.showGameStarted(gameId, playerNicknames);
    }
}
