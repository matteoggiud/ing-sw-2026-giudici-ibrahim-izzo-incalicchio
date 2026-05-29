package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiContext;
import it.polimi.ingsw.common.network.dto.LeadBoardDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreController {

    private static final double BACKGROUND_RATIO = 1672.0 / 941.0;

    private static final double TITLE_X = 0.500;
    private static final double TITLE_Y = 0.108;
    private static final double TITLE_W = 0.310;
    private static final double TITLE_H = 0.070;

    private static final double RANKING_TITLE_X = 0.500;
    private static final double RANKING_TITLE_Y = 0.245;
    private static final double RANKING_TITLE_W = 0.360;
    private static final double RANKING_TITLE_H = 0.055;

    private static final double RANKING_LIST_X = 0.500;
    private static final double RANKING_LIST_Y = 0.425;
    private static final double RANKING_LIST_W = 0.860;
    private static final double RANKING_LIST_H = 0.320;

    private static final double GLOBAL_TITLE_X = 0.500;
    private static final double GLOBAL_TITLE_Y = 0.685;
    private static final double GLOBAL_TITLE_W = 0.360;
    private static final double GLOBAL_TITLE_H = 0.055;

    private static final double PLAYER_POSITION_X = 0.500;
    private static final double PLAYER_POSITION_Y = 0.760;
    private static final double PLAYER_POSITION_W = 0.390;
    private static final double PLAYER_POSITION_H = 0.070;

    private static final double BACK_BUTTON_X = 0.500;
    private static final double BACK_BUTTON_Y = 0.925;
    private static final double BACK_BUTTON_W = 0.150;
    private static final double BACK_BUTTON_H = 0.055;

    private GuiContext guiContext;

    @FXML
    private StackPane scoreRoot;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Pane scoreOverlay;

    @FXML
    private Label titleLabel;

    @FXML
    private Label rankingTitleLabel;

    @FXML
    private ListView<String> rankingListView;

    @FXML
    private Label globalTitleLabel;

    @FXML
    private Label playerPositionLabel;

    @FXML
    private Button backToMenuButton;

    @FXML
    private Label timeoutLabel;

    private final ObservableList<String> rankingItems = FXCollections.observableArrayList();

    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }

    @FXML
    private void initialize() {
        rankingListView.setItems(rankingItems);
        bindScoreLayout();

        if (timeoutLabel != null) {
            timeoutLabel.setVisible(false);
        }
    }

    public void showMatchRankings(Map<String, Integer> finalRanking) {
        Platform.runLater(() -> {
            if (finalRanking == null || finalRanking.isEmpty()) {
                showReconnectTimeoutView();
                return;
            }

            // Otherwise show normal ranking view
            showNormalRankingView();

            List<Map.Entry<String, Integer>> ordered = finalRanking.entrySet().stream()
                    .sorted((e1, e2) -> {
                        int cmp = Integer.compare(e2.getValue(), e1.getValue());
                        if (cmp != 0) return cmp;
                        return e1.getKey().compareToIgnoreCase(e2.getKey());
                    })
                    .toList();

            String me = guiContext != null && guiContext.clientState() != null
                    ? guiContext.clientState().getNickname()
                    : null;

            List<String> rows = new ArrayList<>();
            int pos = 1;

            for (Map.Entry<String, Integer> entry : ordered) {
                String nick = entry.getKey() == null ? "-" : entry.getKey();
                int score = entry.getValue() == null ? 0 : entry.getValue();

                rows.add(String.format("#%d  %s%s  -  %d pt",
                        pos++,
                        nick,
                        me != null && me.equals(nick) ? " (you)" : "",
                        score));
            }

            rankingItems.setAll(rows);
        });
    }

    private void showReconnectTimeoutView() {
        // Run on FX thread already in caller, but ensure safety
        Platform.runLater(() -> {
            // Hide ranking-related nodes
            rankingTitleLabel.setVisible(false);
            rankingListView.setVisible(false);
            globalTitleLabel.setVisible(false);
            playerPositionLabel.setVisible(false);

            // Show timeout message
            if (timeoutLabel != null) {
                timeoutLabel.setVisible(true);
            } else {
                // fallback: show a single list entry stating the reason
                rankingItems.setAll("Some players did not reconnect. The game has been closed.");
                rankingListView.setVisible(true);
            }

            // Update title text if you want
            titleLabel.setText("Game Closed");
        });
    }

    private void showNormalRankingView() {
        Platform.runLater(() -> {
            rankingTitleLabel.setVisible(true);
            rankingListView.setVisible(true);
            globalTitleLabel.setVisible(true);
            playerPositionLabel.setVisible(true);

            if (timeoutLabel != null) {
                timeoutLabel.setVisible(false);
            }
            titleLabel.setText("Final Results");
        });
    }

    public void showGlobalPosition(List<LeadBoardDTO> global) {
        Platform.runLater(() -> {
            String player = guiContext != null && guiContext.clientState() != null
                    ? guiContext.clientState().getNickname()
                    : null;

            if (global != null && !global.isEmpty()) {
                for (LeadBoardDTO board : global) {
                    if (player != null && player.equals(board.nickname())) {
                        playerPositionLabel.setText(String.format("Your position: #%d with %d points",
                                board.position(),
                                board.finalScore()));
                        return;
                    }
                }
            }

            playerPositionLabel.setText("Position not found");
        });
    }

    @FXML
    private void handleBackToMenu() {
        if (guiContext == null || guiContext.clientController() == null) return;

        guiContext.clientController().getAvailableGames();
    }

    private void bindScoreLayout() {
        DoubleBinding imageWidth = Bindings.createDoubleBinding(() -> {
            double rootWidth = scoreRoot.getWidth();
            double rootHeight = scoreRoot.getHeight();

            if (rootWidth <= 0 || rootHeight <= 0) return 0.0;

            return Math.min(rootWidth, rootHeight * BACKGROUND_RATIO);
        }, scoreRoot.widthProperty(), scoreRoot.heightProperty());

        DoubleBinding imageHeight = imageWidth.divide(BACKGROUND_RATIO);

        backgroundImage.fitWidthProperty().bind(imageWidth);
        backgroundImage.fitHeightProperty().bind(imageHeight);

        scoreOverlay.minWidthProperty().bind(imageWidth);
        scoreOverlay.prefWidthProperty().bind(imageWidth);
        scoreOverlay.maxWidthProperty().bind(imageWidth);

        scoreOverlay.minHeightProperty().bind(imageHeight);
        scoreOverlay.prefHeightProperty().bind(imageHeight);
        scoreOverlay.maxHeightProperty().bind(imageHeight);

        bindNode(titleLabel, TITLE_X, TITLE_Y, TITLE_W, TITLE_H, imageWidth, imageHeight);
        bindNode(rankingTitleLabel, RANKING_TITLE_X, RANKING_TITLE_Y, RANKING_TITLE_W, RANKING_TITLE_H, imageWidth, imageHeight);
        bindNode(rankingListView, RANKING_LIST_X, RANKING_LIST_Y, RANKING_LIST_W, RANKING_LIST_H, imageWidth, imageHeight);
        bindNode(globalTitleLabel, GLOBAL_TITLE_X, GLOBAL_TITLE_Y, GLOBAL_TITLE_W, GLOBAL_TITLE_H, imageWidth, imageHeight);
        bindNode(playerPositionLabel, PLAYER_POSITION_X, PLAYER_POSITION_Y, PLAYER_POSITION_W, PLAYER_POSITION_H, imageWidth, imageHeight);
        bindNode(backToMenuButton, BACK_BUTTON_X, BACK_BUTTON_Y, BACK_BUTTON_W, BACK_BUTTON_H, imageWidth, imageHeight);
    }

    private void bindNode(Region node,
                          double centerX,
                          double centerY,
                          double width,
                          double height,
                          DoubleBinding imageWidth,
                          DoubleBinding imageHeight) {
        node.layoutXProperty().bind(imageWidth.multiply(centerX - width / 2.0));
        node.layoutYProperty().bind(imageHeight.multiply(centerY - height / 2.0));

        node.prefWidthProperty().bind(imageWidth.multiply(width));
        node.prefHeightProperty().bind(imageHeight.multiply(height));

        node.minWidthProperty().bind(imageWidth.multiply(width));
        node.minHeightProperty().bind(imageHeight.multiply(height));

        node.maxWidthProperty().bind(imageWidth.multiply(width));
        node.maxHeightProperty().bind(imageHeight.multiply(height));
    }
}
