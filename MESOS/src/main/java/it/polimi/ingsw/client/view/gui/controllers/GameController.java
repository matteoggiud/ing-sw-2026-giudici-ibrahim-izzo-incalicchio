package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiContext;
import it.polimi.ingsw.server.model.observer.dto.BoardDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerDeckDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    private GuiContext guiContext;

    @FXML private Label eraLabel;
    @FXML private Label turnLabel;
    @FXML private Label gameInfoLabel;
    @FXML private TextArea boardArea;

    @FXML private ListView<PlayerInfoDTO> playersListView;

    private final ObservableList<PlayerInfoDTO> playersItems = FXCollections.observableArrayList();

    // mappa nickname -> PlayerDeckDTO; aggiornata da showPlayerDeck(...)
    private final Map<String, PlayerDeckDTO> playerDecks = new HashMap<>();

    // nickname dell'utente locale (viene passato a showPlayersInfo)
    private String myNickname = null;

    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }

    @FXML
    private void initialize() {
        playersListView.setItems(playersItems);

        // Cell factory: per ogni PlayerInfoDTO crea un pannello con stats + deck
        playersListView.setCellFactory(lv -> new PlayerCell());
    }

    public void showGameStarted(int gameId, List<String> playerNicknames) {
        Platform.runLater(() -> {
            gameInfoLabel.setText("Game started. ID: " + gameId + " | Players: " + String.join(", ", playerNicknames));
            turnLabel.setText("Turn: waiting...");
            eraLabel.setText("Era: -");
            boardArea.setText("Board will appear here...");
            // playersItems rimane vuoto fino a onPlayerInfosChanged
        });
    }

    public void showBoard(BoardDTO board) {
        Platform.runLater(() -> {
            eraLabel.setText("Era: " + board.currentEra());
            boardArea.setText(formatBoard(board));
        });
    }

    public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) {
        Platform.runLater(() -> {
            turnLabel.setText(isMyTurn
                    ? "Turn: PLACE TOTEM — it's YOUR turn"
                    : "Turn: PLACE TOTEM — " + currentPlayerNickname + "'s turn");
        });
    }

    public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        Platform.runLater(() -> {
            turnLabel.setText(isMyTurn
                    ? "Turn: RESOLVE ACTION — it's YOUR turn"
                    : "Turn: RESOLVE ACTION — " + currentPlayerNickname + "'s turn");
        });
    }

    public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        Platform.runLater(() -> {
            turnLabel.setText(isMyTurn
                    ? "Turn: EXTRA ACTION — it's YOUR turn"
                    : "Turn: EXTRA ACTION — " + currentPlayerNickname + "'s turn");
        });
    }

    /**
     * Aggiorna la lista dei PlayerInfoDTO (numero giocatori / stats).
     * Chiamato da ViewController.onPlayerInfosChanged -> view.showPlayersInfo(...)
     */
    public void showPlayersInfo(Collection<PlayerInfoDTO> playersInfo, String myNickname) {
        this.myNickname = myNickname;
        Platform.runLater(() -> {
            playersItems.setAll(playersInfo);
            // refresh per far ridisegnare le celle (servirà se deck aggiornati prima)
            playersListView.refresh();
        });
    }

    /**
     * Aggiorna il deck di un singolo giocatore; ridisegna la listView per aggiornare la cella.
     * Chiamato da ViewController.onPlayerDeckUpdated -> view.showPlayerDeck(...)
     */
    public void showPlayerDeck(String nickname, PlayerDeckDTO playerDeck, boolean isMe) {
        Platform.runLater(() -> {
            if (playerDeck == null) {
                playerDecks.remove(nickname);
            } else {
                playerDecks.put(nickname, playerDeck);
            }
            // forzare il refresh delle celle per mostrare il deck aggiornato
            playersListView.refresh();
        });
    }

    public void clearDecks() {
        Platform.runLater(() -> {
            playerDecks.clear();
            playersListView.refresh();
        });
    }

    /* --- formattatori --- */

    private String formatPlayerInfo(PlayerInfoDTO p) {
        String me = (myNickname != null && p.nickname().equals(myNickname)) ? " (you)" : "";
        return p.nickname() + me +
                " | food=" + p.food() +
                " | pp=" + p.pp() +
                " | totem=" + p.totem();
    }

    private String formatDeck(PlayerDeckDTO deck) {
        if (deck == null) return "<no deck>";
        // Sostituisci con il formato utile del tuo PlayerDeckDTO;
        // qui uso toString() come fallback
        return deck.toString();
    }

    private String formatBoard(BoardDTO board) {
        StringBuilder sb = new StringBuilder();
        sb.append("Current era: ").append(board.currentEra()).append("\n\n");
        sb.append("Offer boxes: ").append(board.offerTrackBoxes().size()).append("\n");
        sb.append("Turn slots: ").append(board.turnOrderSlots().size()).append("\n");
        sb.append("Upper character cards: ").append(board.upperCharacterCards().size()).append("\n");
        sb.append("Lower character cards: ").append(board.lowerCharacterCards().size()).append("\n");
        sb.append("Upper event cards: ").append(board.upperEventCards().size()).append("\n");
        sb.append("Lower event cards: ").append(board.lowerEventCards().size()).append("\n");
        sb.append("Upper building cards: ").append(board.upperBuildingCards().size()).append("\n");
        sb.append("Lower building cards: ").append(board.lowerBuildingCards().size()).append("\n");
        return sb.toString();
    }

    /* --- custom ListCell per mostrare stats + deck nello stesso pannello --- */
    private class PlayerCell extends ListCell<PlayerInfoDTO> {
        private final HBox root = new HBox(12);
        private final VBox statsBox = new VBox(4);
        private final VBox deckBox = new VBox(4);
        private final Label nicknameLabel = new Label();
        private final Label statsLabel = new Label();
        private final Label deckLabel = new Label();

        PlayerCell() {
            super();
            statsBox.getChildren().addAll(nicknameLabel, statsLabel);
            deckBox.getChildren().addAll(new Label("Deck:"), deckLabel);

            HBox.setHgrow(statsBox, Priority.ALWAYS);
            root.getChildren().addAll(statsBox, deckBox);
        }

        @Override
        protected void updateItem(PlayerInfoDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                nicknameLabel.setText(item.nickname() + ((myNickname != null && item.nickname().equals(myNickname)) ? " (you)" : ""));
                statsLabel.setText("food=" + item.food() + " | pp=" + item.pp() + " | totem=" + item.totem());
                PlayerDeckDTO deck = playerDecks.get(item.nickname());
                deckLabel.setText(formatDeck(deck));
                setGraphic(root);
            }
        }
    }
}