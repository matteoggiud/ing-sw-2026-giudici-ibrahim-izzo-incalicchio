package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.ClientSession;

public class GameServerImpl implements IGameServerRMI {
    private GameController controller;

    public GameServerImpl(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void login(IGameClientRMI gameClientRMI, String nickname, String password) {
            controller.login(gameClientRMI, nickname, password);
    }
}
