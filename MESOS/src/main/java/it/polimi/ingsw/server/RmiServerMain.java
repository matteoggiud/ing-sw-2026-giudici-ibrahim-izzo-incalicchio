package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;
import it.polimi.ingsw.server.network.rmi.RmiBootstrapServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RmiServerMain {
    public static void main(String[] args) {
        final int rmiPort = 1234;
        final String rmiName = "MESOS_RMI";
        String defaultServerIp;
        String serverIp;
        Scanner scanner = new Scanner(System.in);
        final int bootStrapPort = 1235;

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


        try {
            GamePortal gamePortal = new GamePortal();
            GameController gameController = new GameController(gamePortal);

            RmiBootstrap bootstrap = new RmiBootstrapServer(gameController);

            //We create a remote bootstrap object
            RmiBootstrap stub = (RmiBootstrap) UnicastRemoteObject.exportObject(bootstrap, 1235);

            Registry registry = LocateRegistry.createRegistry(rmiPort);

            //After we created the registry with the port, we bind to it the server name and the remote bootstrap
            registry.rebind(rmiName, stub);

            System.out.println("Rmi server listening on port " + rmiPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
