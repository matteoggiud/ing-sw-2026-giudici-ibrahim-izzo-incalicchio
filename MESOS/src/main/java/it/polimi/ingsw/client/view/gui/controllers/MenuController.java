package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiContext;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.LeadBoardDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private final ObservableList<GameInfoDTO> games = FXCollections.observableArrayList();

    private final ObservableList<LeadBoardDTO> rankingsObs = FXCollections.observableArrayList();


    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }


    @FXML
    private void initialize() {
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
                    String text = String.format("Game #%d — %d/%d players — [%s]",
                            item.gameId(), item.players() == null ? 0 : item.players().size(), item.expectedPlayerCount(), players);
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
                    setText(String.format("#%d %s - %d pt (%s)",
                            item.position(), item.nickname(), item.finalScore(), item.finished_at()));
                }
            }
        });
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