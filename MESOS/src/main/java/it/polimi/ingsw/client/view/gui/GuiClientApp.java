package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.network.NetworkConnectionStrategy;
import it.polimi.ingsw.client.network.RmiConnectionStrategy;
import it.polimi.ingsw.client.network.SocketConnectionStrategy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.List;

public class GuiClientApp extends Application {

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

        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Platform.runLater(() -> {
            try{
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/mesos_lowbar.png")));
            }catch(Exception e){
                System.out.println("Error loading icon: " + e.getMessage());
            }
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.setTitle("MESOS");
        });

        primaryStage.show();


        // 1. Parse command line arguments
        List<String> args = getParameters().getRaw();
        String protocol = args.size() > 0 ? args.get(0) : "SOCKET";
        String host = args.size() > 1 ? args.get(1) : "localhost";

        // 2. Initialize client controller
        ClientState clientState = new ClientState();
        GuiClientView view = new GuiClientView(primaryStage, root);
        // this.guiClientView = view;
        ViewController viewController = new ViewController(clientState, view);


        NetworkConnectionStrategy connectionStrategy = "RMI".equals(protocol) ? new RmiConnectionStrategy() : new SocketConnectionStrategy();
        try {
            // Connect to server and get virtual server proxy
            GenericVirtualServer network = connectionStrategy.connect(host, viewController, clientState, view);

            // Initialize client controller with network connection and the view
            ClientController controller = new ClientController(network);
            view.setGuiContext(new GuiContext(controller, clientState));

            primaryStage.setOnCloseRequest(event -> {
                try {
                    controller.disconnect();
                } catch (Exception ignored) {
                }

                Platform.exit();
                System.exit(0);
            });

            clientState.connect();
            view.showAuthenticator("Login required!");
            MusicManager.startBackgroundMusic();

        } catch (Exception e) {
            System.out.println("Failed to connect to server: " + e.getMessage());
            view.showRoot(new Label("Failed to connect to server"));
        }

    }
}
