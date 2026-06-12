package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiContext;
import it.polimi.ingsw.common.network.dto.GameInfoDTO;
import it.polimi.ingsw.common.network.dto.LeadBoardDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.layout.Region;


import java.util.List;
import java.util.stream.Collectors;

public class MenuController {
    private GuiContext guiContext;

    @FXML
    private ListView<GameInfoDTO> gamesListView;

    @FXML
    private Button joinButton;

    @FXML
    private ChoiceBox<Integer> choiceExpectedNumPlayers;

    @FXML
    private ListView<LeadBoardDTO> rankingsListView;

    @FXML
    private ChoiceBox<Integer> choiceNumPlayersForRankings;

    @FXML
    private StackPane menuRoot;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Pane menuOverlay;

    @FXML
    private Label availableGamesTitle;

    @FXML
    private Label menuTitle;

    @FXML
    private VBox createPanel;

    @FXML
    private VBox playerCountPanel;

    @FXML
    private Button quitButton;

    @FXML
    private Label rankingsTitle;

    @FXML
    private HBox rankingFilter;

    @FXML
    private HBox gamesActions;

    @FXML
    private Button rankingsRefreshButton;


    private final ObservableList<GameInfoDTO> games = FXCollections.observableArrayList();

    private final ObservableList<LeadBoardDTO> rankingsObs = FXCollections.observableArrayList();

    private static final double BACKGROUND_RATIO = 16.0 / 9.0;


    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }


    @FXML
    private void initialize() {
        bindBackgroundCover();
        bindMenuLayout();
        rankingFilter.toFront();
        choiceNumPlayersForRankings.toFront();
        gamesListView.setItems(games);

        gamesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleJoin();
            }
        });

        gamesListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(GameInfoDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String players = item.players() == null ? "" :
                            item.players().stream().collect(Collectors.joining(", "));
                    String text = String.format("Game #%d - %d/%d players - %s",
                            item.gameId(),
                            item.players() == null ? 0 : item.players().size(),
                            item.expectedPlayerCount(),
                            players.isBlank() ? "waiting for players" : players);
                    setText(text);
                }
            }
        });

        // disable join button if no game is selected
        joinButton.disableProperty().bind(gamesListView.getSelectionModel().selectedItemProperty().isNull());

        rankingsListView.setItems(rankingsObs);

        rankingsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(LeadBoardDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("#%d %s - %d pts",
                            item.position(), item.nickname(), item.finalScore()));
                }
            }
        });
    }

    /**
     * Binds the menu layout to the background cover to ensure that the menu elements are always centered and properly spaced
     */
    private void bindBackgroundCover() {
        double imageRatio = backgroundImage.getImage().getWidth() / backgroundImage.getImage().getHeight();

        DoubleBinding imageWidth = Bindings.createDoubleBinding(
                () -> Math.min(menuRoot.getWidth(), menuRoot.getHeight() * imageRatio),
                menuRoot.widthProperty(),
                menuRoot.heightProperty()
        );

        DoubleBinding imageHeight = Bindings.createDoubleBinding(
                () -> Math.min(menuRoot.getHeight(), menuRoot.getWidth() / imageRatio),
                menuRoot.widthProperty(),
                menuRoot.heightProperty()
        );

        backgroundImage.fitWidthProperty().bind(imageWidth);
        backgroundImage.fitHeightProperty().bind(imageHeight);

        menuOverlay.minWidthProperty().bind(imageWidth);
        menuOverlay.prefWidthProperty().bind(imageWidth);
        menuOverlay.maxWidthProperty().bind(imageWidth);

        menuOverlay.minHeightProperty().bind(imageHeight);
        menuOverlay.prefHeightProperty().bind(imageHeight);
        menuOverlay.maxHeightProperty().bind(imageHeight);
    }

    /**
     * Binds the menu elements to specific positions and sizes relative to the menu overlay
     */
    private void bindMenuLayout() {
        bindNode(availableGamesTitle, 0.220, 0.385, 0.28, 0.055);
        bindNode(gamesListView, 0.225, 0.620, 0.295, 0.285);
        bindNode(gamesActions, 0.215, 0.795, 0.245, 0.055);

        bindNode(menuTitle, 0.500, 0.385, 0.15, 0.055);
        bindNode(createPanel, 0.500, 0.495, 0.170, 0.105);
        bindNode(playerCountPanel, 0.500, 0.690, 0.165, 0.105);
        bindNode(quitButton, 0.500, 0.865, 0.160, 0.050);

        bindNode(rankingsTitle, 0.780, 0.385, 0.30, 0.055);
        bindNode(rankingFilter, 0.730, 0.470, 0.185, 0.050);
        bindNode(rankingsListView, 0.775, 0.630, 0.295, 0.280);
        bindNode(rankingsRefreshButton, 0.775, 0.795, 0.170, 0.050);
    }


    /**
     * Helper method to bind a node's size and position relative to the menu overlay.
     * @param node the node to bind
     * @param centerX the horizontal center position as a percentage of the menu overlay width (0.0 to 1.0)
     * @param centerY the vertical center position as a percentage of the menu overlay height (0.0 to 1.0)
     * @param width the width of the node as a percentage of the menu overlay width (0.0 to 1.0)
     * @param height the height of the node as a percentage of the menu overlay height (0.0 to 1.0)
     */
    private void bindNode(Region node, double centerX, double centerY, double width, double height) {
        node.prefWidthProperty().bind(menuOverlay.widthProperty().multiply(width));
        node.prefHeightProperty().bind(menuOverlay.heightProperty().multiply(height));

        node.layoutXProperty().bind(menuOverlay.widthProperty()
                .multiply(centerX)
                .subtract(menuOverlay.widthProperty().multiply(width).divide(2.0)));

        node.layoutYProperty().bind(menuOverlay.heightProperty()
                .multiply(centerY)
                .subtract(menuOverlay.heightProperty().multiply(height).divide(2.0)));
    }



    public void showAvailableGames() {
        Platform.runLater(() -> {
            games.setAll(guiContext.clientState().getAvailableGames());
        });
    }

    public void showRankings(List<LeadBoardDTO> rankingsList) {
        Platform.runLater(() -> rankingsObs.setAll(rankingsList));
    }

    @FXML
    private void handleJoin() {
        GameInfoDTO selected = gamesListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        int gameId = selected.gameId();

        guiContext.clientController().joinGame(gameId);
    }

    @FXML
    private void handleRefresh() {
        guiContext.clientController().getAvailableGames();
    }

    @FXML
    private void handleCreate() {
        int expectedPlayers = choiceExpectedNumPlayers.getValue() == null ? 2 : choiceExpectedNumPlayers.getValue();

        guiContext.clientController().createGame(expectedPlayers);
    }

    @FXML
    private void handleQuit() {
        guiContext.clientController().disconnect();
        System.exit(0);
    }

    @FXML
    private void handleRankings(){
        int numPlayers = choiceNumPlayersForRankings.getValue() == null ? 2 : choiceNumPlayersForRankings.getValue();
        System.out.println("DEBUG: handleRankings called with numPlayers=" + numPlayers);
        guiContext.clientController().getRankings(numPlayers);
    }
}