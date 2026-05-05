package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.socket.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteVirtualServer implements VirtualServer {
    private final GameController gameController;
    private final List<VirtualView> clientViews;

    public RemoteVirtualServer(GameController gameController) {
        this.gameController = gameController;
        this.clientViews = new ArrayList<>();
    }

    /**
     * Connects a client view to the server, allowing it to receive updates and interact with the game.
     *
     * @param clientView the client view to connect
     * @throws RemoteException if a communication error occurs during the remote method call
     */
    public void connect(VirtualView clientView) throws RemoteException {
        synchronized (clientViews) {
            clientViews.add(clientView);
            //creo client session
        }
    }

    /**
     * Disconnects a client view from the server, preventing it from receiving further updates and interacting with the game.
     *
     * @param clientView the client view to disconnect
     * @throws RemoteException if a communication error occurs during the remote method call
     */
    public void disconnect(VirtualView clientView) throws RemoteException {
        synchronized (clientViews) {
            clientViews.remove(clientView);
        }
    }

    /**
     * Requests the server to log in the user with the given nickname and password hash.
     *
     * @param nickname     the nickname of the user trying to log in
     * @param passwordHash the hash of the user's password
     * @throws RemoteException if a communication error occurs during the remote method call
     */
    @Override
    public void login(String nickname, String passwordHash) throws RemoteException {

        //gameController.login(clientSession, nickname, passwordHash);

    }

    /**
     * Requests the server to register a new user with the given nickname and password hash.
     *
     * @param nickname     the nickname of the user trying to register
     * @param passwordHash the hash of the user's password
     * @throws RemoteException if a communication error occurs during the remote method call
     */
    @Override
    public void register(String nickname, String passwordHash) throws RemoteException {

    }

    /**
     * Requests the server to create a new game with the given host nickname and expected number of players.
     *
     * @param hostNickname       the nickname of the user hosting the game
     * @param expectedNumPlayers the expected number of players for the game
     * @throws RemoteException if a communication error occurs during the remote method call
     */
    @Override
    public void createGame(String hostNickname, int expectedNumPlayers) throws RemoteException {

    }
}
