package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;
import it.polimi.ingsw.server.network.rmi.RmiBootstrapServer;
import it.polimi.ingsw.server.network.socket.SocketAcceptor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        final int socketPort = 12345;

        final int rmiPort = 1234;
        final String rmiName = "MESOS_RMI";

        String defaultServerIp;
        String serverIp;
        Scanner scanner = new Scanner(System.in);

        try {
            defaultServerIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            defaultServerIp = "localhost";
        }

        if (args.length >= 1) {
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
