package it.polimi.ingsw.server;

import it.polimi.ingsw.client.network.InetAddressLookup;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.database.DataDepository;
import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.database.DatabaseBootstrap;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.gamelogic.GameBuilder;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.observer.dto.GameSnapshotDTO;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;
import it.polimi.ingsw.server.network.rmi.RmiBootstrapServer;
import it.polimi.ingsw.server.network.socket.SocketAcceptor;
import it.polimi.ingsw.utils.ConfigurationManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerMain {
    private static Registry rmiRegistry;
    private static RmiBootstrapServer rmiBootstrapServer;
    private static RmiBootstrap rmiBootstrapStub;

    public static void main(String[] args) {
        ConfigurationManager config = ConfigurationManager.getServerInstance();
        final int socketPort = config.getIntProperty("server.port.socket", 12345);

        final int rmiPort = config.getIntProperty("server.port.rmi", 1234);
        final String rmiName = config.getProperty("server.rmi.name", "MESOS_RMI");

        String defaultServerIp = InetAddressLookup.getLocalHostAddress();
        String serverIp = defaultServerIp;
        Scanner scanner = new Scanner(System.in);


        boolean fastStart = config.getBooleanProperty("server.fast_start", false);

        if (args.length >= 1 && !args[0].isBlank()) {
            serverIp = args[0];
        } else if (!fastStart) {
            System.out.print("Server ip [" + defaultServerIp + "]: ");
            String line = scanner.nextLine().trim();
            if (!line.isBlank()) {
                serverIp = line;
            }
        } else {
            System.out.println("Fast start enabled. Using default server ip: " + defaultServerIp);
        }


        System.setProperty("java.rmi.server.hostname", serverIp);


        GamePortal gamePortal = new GamePortal();
        GameController controller = new GameController(gamePortal);
        ScheduledExecutorService disconnectionMonitor = Executors.newSingleThreadScheduledExecutor();
        disconnectionMonitor.scheduleAtFixedRate(controller::disconnectUnreachableSessions, 5, 5, TimeUnit.SECONDS);

        new DatabaseBootstrap().bootstrap();

        DataDepository dataDepo = new DataDepository();

        List<Integer> pendingGames = dataDepo.getInProgressGames();

        if (!pendingGames.isEmpty()) {
            boolean restore;
            if (fastStart) {
                restore = config.getBooleanProperty("server.reload_games_on_start", false);
                System.out.println("Fast start: reloading " + pendingGames.size() + " games in progress = " + restore);
            } else {
                System.out.println("Find " + pendingGames.size() + " games in progress. Do you want to restore them? (y/n)");
                String restoreChoice = scanner.nextLine().trim().toLowerCase();
                restore = restoreChoice.equals("y");
            }

            if (restore) {
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
            rmiBootstrapServer = new RmiBootstrapServer(controller);

            //We create a remote bootstrap object
            rmiBootstrapStub = (RmiBootstrap) UnicastRemoteObject.exportObject(rmiBootstrapServer, 0);

            rmiRegistry = LocateRegistry.createRegistry(rmiPort);

            //After we created the registry with the port, we bind to it the server name and the remote bootstrap
            rmiRegistry.rebind(rmiName, rmiBootstrapStub);

            System.out.println("[SERVER] Server rmi started at " + serverIp + ":" + rmiPort);
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in ServerMain: " + e.getMessage());
        }
    }
}

