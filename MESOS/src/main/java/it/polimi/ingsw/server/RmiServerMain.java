package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.network.rmi.RmiBootstrap;
import it.polimi.ingsw.server.network.rmi.RmiBootstrapServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServerMain {
    public static void main(String[] args){
        final int rmiPort = 1234;
        final String rmiName = "MESOS_RMI";

        try{
            GamePortal gamePortal = new GamePortal();
            GameController gameController = new GameController(gamePortal);

            RmiBootstrap bootstrap = new RmiBootstrapServer(gameController);
            RmiBootstrap stub = (RmiBootstrap) UnicastRemoteObject.exportObject(bootstrap, rmiPort);

            Registry registry = LocateRegistry.createRegistry(rmiPort);
            registry.rebind(rmiName, stub);

            System.out.println("Rmi server listening on port " + rmiPort);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
