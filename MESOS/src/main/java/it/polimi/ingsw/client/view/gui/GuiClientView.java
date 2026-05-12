package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.gui.controllers.*;
import it.polimi.ingsw.server.model.observer.dto.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class GuiClientView implements ClientView {
    private final Stage stage;

    private GuiContext guiContext;

    private Scene menuScene;
    private MenuController menuController;

    private Scene authScene;
    private AuthController authController;

    private Scene lobbyScene;
    private LobbyController lobbyController;

    private Scene gameScene;
    private GameController gameController;

    private Scene endGameScene;
    private ScoreController ScoreController;

    private Map<String, Integer> pendingFinalRanking = Map.of();
    private List<LeadBoardDTO> pendingGlobalRankings = List.of();

    private boolean resolvingShowing = false;


    public GuiClientView(Stage stage) {
        this.stage = stage;
    }


    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }

    private void ensureMenuLoaded() throws IOException {
        if (menuScene != null && menuController != null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPane.fxml"));
        Parent root = loader.load();

        menuController = loader.getController();
        menuController.setGuiContext(guiContext);

        menuScene = new Scene(root);
    }

    private void ensureAuthLoaded() throws IOException {
        if (authScene != null && authController != null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuthPane.fxml"));
        Parent root = loader.load();

        authController = loader.getController();
        authController.setGuiContext(guiContext);

        authScene = new Scene(root);
    }

    private void ensureLobbyLoaded() throws IOException {
        if (lobbyScene != null && lobbyController != null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LobbyPane.fxml"));
        Parent root = loader.load();

        lobbyController = loader.getController();
        lobbyController.setGuiContext(guiContext);

        lobbyScene = new Scene(root);
    }

    private void ensureGameLoaded() throws IOException {
        if (gameScene != null && gameController != null) return;


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GamePane.fxml"));
        Parent root = loader.load();

        gameController = loader.getController();
        gameController.setGuiContext(guiContext);
        gameController.setSeeResultsRequested(this::openScorePane);

        gameScene = new Scene(root);
    }

    private void changeSceneIfNeeded(Scene newScene) {
        if (newScene == null) return;

        if (stage.getScene() != null && stage.getScene() != newScene) {
            stage.setScene(newScene);
        }
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();

        Platform.runLater(() -> stage.setMaximized(true));
    }

    private void ensureScoreLoaded() throws IOException {
        if (endGameScene != null && ScoreController != null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ScorePane.fxml"));
        Parent root = loader.load();

        ScoreController = loader.getController();
        ScoreController.setGuiContext(guiContext);

        endGameScene = new Scene(root);
    }


    /**
     * @param message
     */
    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> {
               /* if (menuController != null) {
                    menuController.showMessage(message);
                } else if (lobbyController != null) {
                    lobbyController.showMessage(message);
                } else if (gameController != null) {
                    gameController.showMessage(message);
                }*/
            System.out.println(message);
        });
    }

    /**
     * @param message
     */
    @Override
    public void showError(String message) {
        Platform.runLater(() -> {
            System.err.println("Error: " + message);

            if (authController != null) {
                authController.showErrorMessage(message);
            }
        });
    }

    /**
     * @param games
     */
    @Override
    public void showAvailableGames(List<GameInfoDTO> games) {
        Platform.runLater(() -> {
            try {
                ensureMenuLoaded();

                menuController.showAvailableGames();

                changeSceneIfNeeded(menuScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @param gameInfo
     */
    @Override
    public void showGameInfo(GameInfoDTO gameInfo) {
        Platform.runLater(() -> {
            try {
                ensureLobbyLoaded();

                lobbyController.showGameInfo(gameInfo);

                changeSceneIfNeeded(lobbyScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * @param gameId
     * @param playerNicknames
     */
    @Override
    public void showGameStarted(int gameId, List<String> playerNicknames) {
        Platform.runLater(() -> {
            try {
                ensureGameLoaded();

                gameController.showGameStarted(gameId, playerNicknames);

                changeSceneIfNeeded(gameScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @param board
     */
    @Override
    public void showBoardSummary(BoardDTO board) {
        if (resolvingShowing) return;
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.showBoard(board);
            }
        });
    }

    /**
     * @param currentPlayerNickname
     * @param isMyTurn
     */
    @Override
    public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.showPlaceTotemTurn(currentPlayerNickname, isMyTurn);
            }
        });
    }

    /**
     * @param currentPlayerNickname
     * @param isMyTurn
     */
    @Override
    public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.showResolveActionTurn(currentPlayerNickname, isMyTurn);
            }
        });
    }

    /**
     * @param currentPlayerNickname
     * @param isMyTurn
     */
    @Override
    public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.showExtraActionTurn(currentPlayerNickname, isMyTurn);
            }
        });
    }

    /**
     * @param playerNickname
     * @param food
     * @param prestigePoints
     * @param isMe
     */
    @Override
    public void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe) {
    }

    /**
     * @param playerNickname
     * @param playerDeck
     * @param isMe
     */
    @Override
    public void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe) {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.showPlayerDeck(playerNickname, playerDeck, isMe);
            }
        });
    }


    /**
     * @param playersInfo
     * @param myNickname
     */
    @Override
    public void showPlayersInfo(Collection<PlayerInfoDTO> playersInfo, String myNickname) {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.showPlayersInfo(playersInfo, myNickname);
            }
        });
    }

    /**
     * @param rankings
     */
    @Override
    public void showRankings(List<LeadBoardDTO> rankings) {
        Platform.runLater(() -> {
            try {
                ensureMenuLoaded();

                menuController.showRankings(rankings);

                changeSceneIfNeeded(menuScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void showAuthenticator(String message) {
        Platform.runLater(() -> {
            try {
                ensureAuthLoaded();

                authController.showInfoMessage(message);


                changeSceneIfNeeded(authScene);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void showResolvingEvent(int gameId, EventCardDTO eventCard) {
        Platform.runLater(() -> {
            // if we are not already showing resolving events, start the sequence
            if (!resolvingShowing) {
                resolvingShowing = true;
                showNextResolvingEvent();
            }
        });
    }

    private void showNextResolvingEvent() {

        EventCardDTO next = guiContext.clientState().popEventCard();
        if (next == null) {
            resolvingShowing = false;
            showBoardSummary(guiContext.clientState().getBoard());
            if (gameController != null && guiContext != null && guiContext.clientState() != null) {
                BoardDTO board = guiContext.clientState().getBoard();
                if (board != null) {
                    gameController.showBoard(board);
                }
            }
            return;
        }

        try {
            // load FXML and the popup controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EventPopupPane.fxml"));
            StackPane root = loader.load();

            EventPopupController controller = loader.getController();
            controller.populateEventCard(next);

            // popup stage transparent
            javafx.stage.Stage popup = new javafx.stage.Stage(javafx.stage.StageStyle.TRANSPARENT);
            popup.initOwner(stage);
            popup.setAlwaysOnTop(true);
            popup.setResizable(false);

            javafx.scene.Scene scene = new javafx.scene.Scene(root, javafx.scene.paint.Color.TRANSPARENT);
            popup.setScene(scene);

            // popup dimensions
            double popupWidth = 360;
            double popupHeight = 520;

            // center popup on owner stage
            double ownerX = stage.getX();
            double ownerY = stage.getY();
            double ownerW = stage.getWidth();
            double ownerH = stage.getHeight();

            popup.setX(ownerX + (ownerW - popupWidth) / 2);
            popup.setY(ownerY + (ownerH - popupHeight) / 2);

            // animation sequence: fade in, pause, fade out
            javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(
                    javafx.util.Duration.millis(220), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(3));

            javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(
                    javafx.util.Duration.millis(300), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // allow click to skip animation and show next immediately
            root.setOnMouseClicked(ev -> {
                fadeIn.stop();
                pause.stop();
                fadeOut.stop();
                popup.close();
                // skip directly to the next event
                showNextResolvingEvent();
            });

            // chain animations
            fadeIn.setOnFinished(ev -> pause.play());
            pause.setOnFinished(ev -> fadeOut.play());
            fadeOut.setOnFinished(ev -> {
                popup.close();
                // skip directly to the next event
                showNextResolvingEvent();
            });

            popup.show();
            fadeIn.play();

        } catch (IOException e) {
            e.printStackTrace();
            // in case of error, try to show the next event to avoid blocking the sequence
            showNextResolvingEvent();
        }

    }

    private void openScorePane() {
        Platform.runLater(() -> {
            try {
                ensureScoreLoaded();

                ScoreController.showMatchRankings(pendingFinalRanking);
                ScoreController.showGlobalPosition(pendingGlobalRankings);

                changeSceneIfNeeded(endGameScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void showGameEnded() {
        Platform.runLater(() -> {
            try {
                ensureScoreLoaded();

                // Pick the final Ranking of the Game
                Map<String, Integer> finalRanking = guiContext != null && guiContext.clientState() != null
                        ? guiContext.clientState().getFinalRanking()
                        : Map.of();

                List<LeadBoardDTO> global = guiContext != null && guiContext.clientState() != null
                        ? guiContext.clientState().getHistoricalRankings()
                        : List.of();

                this.pendingFinalRanking = finalRanking;
                this.pendingGlobalRankings = global;

                if (gameController != null) {
                    gameController.showSeeResultsButton();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
