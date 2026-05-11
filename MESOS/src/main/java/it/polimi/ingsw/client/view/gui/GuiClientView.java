package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.gui.controllers.*;
import it.polimi.ingsw.server.model.observer.dto.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
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
        stage.show();

        Platform.runLater(() -> {
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        });
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
