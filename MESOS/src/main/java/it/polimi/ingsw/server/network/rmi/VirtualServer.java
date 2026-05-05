package it.polimi.ingsw.server.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    /**
     * Requests the server to log in the user with the given nickname and password hash.
     *
     * @param nickname     the nickname of the user trying to log in
     * @param passwordHash the hash of the user's password
     * @throws RemoteException if a communication error occurs during the remote method call
     */
    void login(String nickname, String passwordHash) throws RemoteException;

    /**
     * Requests the server to register a new user with the given nickname and password hash.
     *
     * @param nickname     the nickname of the user trying to register
     * @param passwordHash the hash of the user's password
     * @throws RemoteException if a communication error occurs during the remote method call
     */
    void register(String nickname, String passwordHash) throws RemoteException;

    /**
     * Requests the server to create a new game with the given host nickname and expected number of players.
     *
     * @param hostNickname       the nickname of the user hosting the game
     * @param expectedNumPlayers the expected number of players for the game
     * @throws RemoteException if a communication error occurs during the remote method call
     */
    void createGame(String hostNickname, int expectedNumPlayers) throws RemoteException;

}
