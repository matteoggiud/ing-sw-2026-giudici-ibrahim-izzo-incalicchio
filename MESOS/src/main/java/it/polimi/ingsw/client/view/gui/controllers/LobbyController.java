package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiContext;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class LobbyController {
    private GuiContext guiContext;
    private GameInfoDTO currentGameInfo;

    @FXML
    private Label gameIdLabel;

    @FXML
    private Label playersCountLabel;

    @FXML
    private ListView<String> playersListView;

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
            playersListView.getItems().setAll(gameInfo.players());
        });
    }


    @FXML
    private void handleLeave() {
        if (guiContext != null) {
            guiContext.clientController().leftGame();
        }
    }
}