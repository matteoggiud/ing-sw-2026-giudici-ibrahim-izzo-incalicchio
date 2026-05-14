package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiContext;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class LobbyController {
    private static final double BACKGROUND_RATIO = 16.0 / 9.0;

    private static final double TITLE_X = 0.500;
    private static final double TITLE_Y = 0.212;
    private static final double TITLE_W = 0.270;
    private static final double TITLE_H = 0.070;

    private static final double GAME_ID_X = 0.500;
    private static final double GAME_ID_Y = 0.305;
    private static final double GAME_ID_W = 0.220;
    private static final double GAME_ID_H = 0.050;

    private static final double PLAYERS_COUNT_X = 0.500;
    private static final double PLAYERS_COUNT_Y = 0.372;
    private static final double PLAYERS_COUNT_W = 0.220;
    private static final double PLAYERS_COUNT_H = 0.050;

    private static final double PLAYERS_TITLE_X = 0.500;
    private static final double PLAYERS_TITLE_Y = 0.455;
    private static final double PLAYERS_TITLE_W = 0.300;
    private static final double PLAYERS_TITLE_H = 0.055;

    private static final double PLAYERS_LIST_X = 0.500;
    private static final double PLAYERS_LIST_Y = 0.645;
    private static final double PLAYERS_LIST_W = 0.860;
    private static final double PLAYERS_LIST_H = 0.280;

    private static final double LEAVE_BUTTON_X = 0.500;
    private static final double LEAVE_BUTTON_Y = 0.915;
    private static final double LEAVE_BUTTON_W = 0.120;
    private static final double LEAVE_BUTTON_H = 0.055;

    private GuiContext guiContext;
    private GameInfoDTO currentGameInfo;

    @FXML
    private StackPane lobbyRoot;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Pane lobbyOverlay;

    @FXML
    private Label lobbyTitleLabel;

    @FXML
    private Label gameIdLabel;

    @FXML
    private Label playersCountLabel;

    @FXML
    private Label playersTitleLabel;

    @FXML
    private ListView<String> playersListView;

    @FXML
    private Button leaveButton;

    private final ObservableList<String> playersList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        bindBackgroundCover();
        bindLobbyLayout();

        playersListView.setItems(playersList);
    }

    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }

    public void showGameInfo(GameInfoDTO gameInfo) {
        this.currentGameInfo = gameInfo;

        Platform.runLater(() -> {
            gameIdLabel.setText("Game ID: " + gameInfo.gameId());
            playersCountLabel.setText(
                    "Players: " + gameInfo.numOfPlayers() + " / " + gameInfo.expectedPlayerCount()
            );

            playersList.setAll(gameInfo.players());
        });
    }

    private void bindBackgroundCover() {
        double imageRatio = backgroundImage.getImage().getWidth() / backgroundImage.getImage().getHeight();

        DoubleBinding imageWidth = Bindings.createDoubleBinding(
                () -> Math.min(lobbyRoot.getWidth(), lobbyRoot.getHeight() * imageRatio),
                lobbyRoot.widthProperty(),
                lobbyRoot.heightProperty()
        );

        DoubleBinding imageHeight = Bindings.createDoubleBinding(
                () -> Math.min(lobbyRoot.getHeight(), lobbyRoot.getWidth() / imageRatio),
                lobbyRoot.widthProperty(),
                lobbyRoot.heightProperty()
        );

        backgroundImage.fitWidthProperty().bind(imageWidth);
        backgroundImage.fitHeightProperty().bind(imageHeight);

        lobbyOverlay.minWidthProperty().bind(imageWidth);
        lobbyOverlay.prefWidthProperty().bind(imageWidth);
        lobbyOverlay.maxWidthProperty().bind(imageWidth);

        lobbyOverlay.minHeightProperty().bind(imageHeight);
        lobbyOverlay.prefHeightProperty().bind(imageHeight);
        lobbyOverlay.maxHeightProperty().bind(imageHeight);
    }


    private void bindLobbyLayout() {
        bindNode(lobbyTitleLabel, TITLE_X, TITLE_Y, TITLE_W, TITLE_H);
        bindNode(gameIdLabel, GAME_ID_X, GAME_ID_Y, GAME_ID_W, GAME_ID_H);
        bindNode(playersCountLabel, PLAYERS_COUNT_X, PLAYERS_COUNT_Y, PLAYERS_COUNT_W, PLAYERS_COUNT_H);
        bindNode(playersTitleLabel, PLAYERS_TITLE_X, PLAYERS_TITLE_Y, PLAYERS_TITLE_W, PLAYERS_TITLE_H);
        bindNode(playersListView, PLAYERS_LIST_X, PLAYERS_LIST_Y, PLAYERS_LIST_W, PLAYERS_LIST_H);
        bindNode(leaveButton, LEAVE_BUTTON_X, LEAVE_BUTTON_Y, LEAVE_BUTTON_W, LEAVE_BUTTON_H);
    }

    private void bindNode(Region node, double centerX, double centerY, double width, double height) {
        node.prefWidthProperty().bind(lobbyOverlay.widthProperty().multiply(width));
        node.prefHeightProperty().bind(lobbyOverlay.heightProperty().multiply(height));

        node.layoutXProperty().bind(lobbyOverlay.widthProperty()
                .multiply(centerX)
                .subtract(lobbyOverlay.widthProperty().multiply(width).divide(2.0)));

        node.layoutYProperty().bind(lobbyOverlay.heightProperty()
                .multiply(centerY)
                .subtract(lobbyOverlay.heightProperty().multiply(height).divide(2.0)));
    }

    @FXML
    private void handleLeave() {
        if (guiContext != null) {
            guiContext.clientController().leftLobby();
        }
    }
}
