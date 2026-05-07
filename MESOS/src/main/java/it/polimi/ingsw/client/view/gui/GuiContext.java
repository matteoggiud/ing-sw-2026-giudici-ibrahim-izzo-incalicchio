package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.socket.ClientState;

public record GuiContext(ClientController clientController, ViewController viewController, ClientState clientState) {
}
