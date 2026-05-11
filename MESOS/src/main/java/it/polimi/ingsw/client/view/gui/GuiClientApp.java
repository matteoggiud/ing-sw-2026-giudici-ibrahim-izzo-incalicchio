package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.NetworkBuilder;
import it.polimi.ingsw.client.network.socket.ClientState;
import it.polimi.ingsw.client.view.ClientView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;

public class GuiClientApp extends Application {
    private ClientState clientState;

    private ViewController viewController;


    @Override
    public void init() throws Exception {
        //TODO: we do here the "client main" of the gui

        // this.clientController = new ClientController(virtualServer);

    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mesos");

        primaryStage.setScene(new Scene(new Label("Connecting to server...")));
        primaryStage.show();

        // 1. Parse command line arguments
        List<String> args = getParameters().getRaw();
        String protocol = args.size() > 0 ? args.get(0) : "SOCKET";
        String host = args.size() > 1 ? args.get(1) : "localhost";
        int port = args.size() > 2 ? Integer.parseInt(args.get(2)) : 12345;

        // 2. Initialize client controller
        this.clientState = new ClientState();
        GuiClientView view = new GuiClientView(primaryStage);
        // this.guiClientView = view;
        this.viewController = new ViewController(clientState, view);


        NetworkBuilder builder = new NetworkBuilder(host, viewController);
        if ("SOCKET".equals(protocol)) {
            builder.createSocketConnection(clientState, view);
        } else if ("RMI".equals(protocol)) {
            builder.createRmiConnection(clientState, view);
        }

        clientState.connect();

        primaryStage.setTitle("Mesos");


    }
}
