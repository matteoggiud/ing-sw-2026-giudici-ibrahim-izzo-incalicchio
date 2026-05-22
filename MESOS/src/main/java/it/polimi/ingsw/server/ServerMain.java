package it.polimi.ingsw.server;

import it.polimi.ingsw.client.network.InetAddressLookup;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.database.DataDepository;
import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.gamelogic.GameBuilder;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.observer.dto.GameSnapshotDTO;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;
import it.polimi.ingsw.server.network.rmi.RmiBootstrapServer;
import it.polimi.ingsw.server.network.socket.SocketAcceptor;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class
ServerMain {
    public static void main(String[] args) {
        final int socketPort = 12345;

        final int rmiPort = 1234;
        final String rmiName = "MESOS_RMI";

        String defaultServerIp = InetAddressLookup.getLocalHostAddress();
        String serverIp;
        Scanner scanner = new Scanner(System.in);


        if (args.length >= 1 && !args[0].isBlank()) {
            serverIp = args[0];
        } else {
            System.out.print("Server ip [" + defaultServerIp + "]: ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                serverIp = defaultServerIp;
            } else {
                serverIp = line;
            }
        }

        System.setProperty("java.rmi.server.hostname", serverIp);


        GamePortal gamePortal = new GamePortal();
        GameController controller = new GameController(gamePortal);

        DataDepository dataDepo = new DataDepository();

        List<Integer> pendingGames = dataDepo.getInProgressGames();

        if (!pendingGames.isEmpty()) {
            System.out.println("Find " + pendingGames.size() + " games in progress. Do you want to restore them? (y/n)");
            String restoreChoice = scanner.nextLine().trim().toLowerCase();
            if (restoreChoice.equals("y")) {
                for (int pendingGamesId : pendingGames) {
                    GameSnapshotDTO snapshotDTO = dataDepo.loadGameSnapshot(pendingGamesId);
                    if (snapshotDTO != null) {
                        Game restoredGame = GameBuilder.reconstructGame(snapshotDTO, dataDepo);
                        gamePortal.restoreGame(restoredGame);
                        System.out.println("Game " + pendingGamesId + " restored.");
                    } else {
                        new DataWriting().updateGameInProgress(pendingGamesId, false);
                        System.out.println("Game " + pendingGamesId + " could not be restored and was closed.");
                    }
                }
            } else {
                // Set pending games as not in progress
                for (int pendingGamesId : pendingGames) {
                    new DataWriting().updateGameInProgress(pendingGamesId, false);
                    System.out.println("Game " + pendingGamesId + " closed.");
                }
            }
        }

        try {
            //SOCKET

            ServerSocket serverSocket = new ServerSocket(socketPort);
            SocketAcceptor socketServer = new SocketAcceptor(serverSocket, controller);

            System.out.println("[SERVER] Server socket started at " + serverIp + ":" + socketPort);

            // socket thread acceptor
            new Thread(() -> {
                try {
                    socketServer.run();
                } catch (IOException e) {
                    System.err.println("[ERROR] Exception caught in ServerMain (Socket thread): " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }).start();

            //RMI
            RmiBootstrap bootstrap = new RmiBootstrapServer(controller);

            //We create a remote bootstrap object
            RmiBootstrap stub = (RmiBootstrap) UnicastRemoteObject.exportObject(bootstrap, 0);

            Registry registry = LocateRegistry.createRegistry(rmiPort);

            //After we created the registry with the port, we bind to it the server name and the remote bootstrap
            registry.rebind(rmiName, stub);

            System.out.println("[SERVER] Server rmi started at " + serverIp + ":" + rmiPort);
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in ServerMain: " + e.getMessage());
        }
    }
}

