package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.network.socket.SocketAcceptor;

import java.net.ServerSocket;

public class ServerMain {
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            GamePortal gamePortal = new GamePortal();
            GameController controller = new GameController(gamePortal);
            SocketAcceptor socketServer = new SocketAcceptor(serverSocket, controller);

            System.out.println("[SERVER] Listening on port " + port + "...");
            socketServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
