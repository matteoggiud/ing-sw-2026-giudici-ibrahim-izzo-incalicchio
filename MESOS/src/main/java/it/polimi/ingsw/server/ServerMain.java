package it.polimi.ingsw.server;

import it.polimi.ingsw.client.network.InetAddressLookup;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.database.DataDepository;
import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.observer.dto.GameSnapshotDTO;
import it.polimi.ingsw.server.model.observer.dto.mapper.GameSnapshotDTOMapper;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;
import it.polimi.ingsw.server.network.rmi.RmiBootstrapServer;
import it.polimi.ingsw.server.network.socket.SocketAcceptor;
// import it.polimi.ingsw.server.model.states.WaitingForReconnectState; // Assicurati di importarlo

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
        for (Integer pendingGame : pendingGames) {
            GameSnapshotDTO gameSnapshot = dataDepo.loadGameSnapshot(pendingGame); 
            
            // 1. Ricostruzione dell'oggetto Game tramite un metodo (da implementare) che mappa i DTO sul modello
            Game reconstructedGame = GameSnapshotDTOMapper.reconstructGame(gameSnapshot);
            
            // 2. Imposta lo stato a WaitingForReconnectState (passandogli il nome dello stato originario)
            // WaitingForReconnectState reconnectState = new WaitingForReconnectState(gameSnapshot.gameState());
            // reconstructedGame.setGameState(reconnectState);
            
            // 3. Aggiungi il game ricostruito al GamePortal / GameManager
            // gamePortal.getGameManager().restoreGame(reconstructedGame);
            
            // 4. Fai partire la fase di attesa (e di conseguenza il timer dei 5 minuti)
            // reconnectState.executePhase(reconstructedGame);
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
            e.printStackTrace();
        }
    }
}
