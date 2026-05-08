package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiContext;
import it.polimi.ingsw.client.view.gui.ImageCache;
import it.polimi.ingsw.server.model.observer.dto.BoardDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerDeckDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    private GuiContext guiContext;

    private static final int IMG_WIDTH = 100;
    private static final int IMG_HEIGHT = 160;

    @FXML
    private Label eraLabel;
    @FXML
    private Label turnLabel;
    @FXML
    private Label gameInfoLabel;
    @FXML
    private ListView<PlayerInfoDTO> playersListView;

    // nel GameController (aggiungi i campi @FXML all'inizio della classe)
    @FXML
    private HBox upperRow;
    @FXML
    private HBox offerTrack;
    @FXML
    private VBox turnOrder;
    @FXML
    private VBox turnOrderBox;
    @FXML
    private HBox lowerRow;

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

    private void configureCardNode(ImageView iv, int cardId, boolean isUpper) {
        iv.setOnMouseEntered(e -> {
            iv.setScaleX(1.05);
            iv.setScaleY(1.05);
        });
        iv.setOnMouseExited(e -> {
            iv.setScaleX(1.0);
            iv.setScaleY(1.0);
        });
        iv.setOnMouseClicked(e -> {
            if (guiContext != null && guiContext.clientController() != null) {
                // esempio: drawCard(selectedCardId, fromUpperRow)
                guiContext.clientController().drawCard(cardId, isUpper);
            }
        });
    }

    private StackPane createOfferBoxNode(int boxIndex, String imageKey) {
        String img = "/assets/offer/offer_box.png";
        ImageView boxView = ImageCache.view(img, 120, 120);
        StackPane stack = new StackPane(boxView);
        // se boxDto ha totem:
        // ImageView totem = ImageCache.view("/assets/totems/totem_blue.png", 32, 32);
        // StackPane.setAlignment(totem, Pos.TOP_RIGHT);
        // stack.getChildren().add(totem);
        stack.setOnMouseClicked(e -> {
            // es. guiContext.getClientController().placeTotem(boxIndex);
        });
        return stack;
    }

    private VBox createTurnSlotNode(int slotIndex, String playerNickname) {
        String img = "/assets/turn/turn_slot.png";
        ImageView slotView = ImageCache.view(img, 120, 60);
        Label name = new Label(/*slotDto.playerNickname() or "-"*/ "-");
        VBox box = new VBox(4, slotView, name);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void populateBoard(BoardDTO board) {
        Platform.runLater(() -> {
            // header
            eraLabel.setText("Era: " + board.currentEra());

            // UPPER: for each card in upperCharacterCards, create an ImageView and add to upperRow
            upperRow.getChildren().clear();
            board.upperCharacterCards().forEach(cardDto -> {
                // creates an image view for the card; adjust path and size as needed
                ImageView imageView = new ImageView();
                try {
                    imageView = ImageCache.view("/assets/cards/" + cardDto.imageKey() + ".png", IMG_WIDTH, IMG_HEIGHT);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error loading image for card " + cardDto.cardId() + ": " + e.getMessage());
                }
                configureCardNode(imageView, cardDto.cardId(), true);
                upperRow.getChildren().add(imageView);
            });
            board.upperEventCards().forEach(cardDto -> {
                // creates an image view for the card; adjust path and size as needed
                ImageView imageView = new ImageView();
                try {
                    imageView = ImageCache.view("/assets/cards/" + cardDto.imageKey() + ".png", IMG_WIDTH, IMG_HEIGHT);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error loading image for card " + cardDto.cardId() + ": " + e.getMessage());
                }
                //configureCardNode(imageView, cardDto.cardId(), true);
                upperRow.getChildren().add(imageView);
            });
            board.upperBuildingCards().forEach(cardDto -> {
                // creates an image view for the card; adjust path and size as needed
                ImageView imageView = new ImageView();
                try {
                    imageView = ImageCache.view("/assets/cards/" + cardDto.imageKey() + ".png", IMG_WIDTH, IMG_HEIGHT);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error loading image for card " + cardDto.cardId() + ": " + e.getMessage());
                }
                configureCardNode(imageView, cardDto.cardId(), true);
                upperRow.getChildren().add(imageView);
            });

            /*
            // OFFER TRACK:TODO
            offerTrack.getChildren().clear();
            board.offerTrackBoxes().forEach(boxDto -> {
                StackPane boxNode = createOfferBoxNode(boxDto.position(), boxDto.imageKey());
                offerTrack.getChildren().add(boxNode);
            });

            // TURN ORDER:TODO
            turnOrder.getChildren().clear();
            board.turnOrderSlots().forEach(slotDto -> {
                VBox slotNode = createTurnSlotNode(slotDto.position(), slotDto.placedPlayerNickname());
                turnOrder.getChildren().add(slotNode);
            });
*/
            // LOWER: for each card in lowerCharacterCards, create an ImageView and add to lowerRow
            lowerRow.getChildren().clear();
            board.lowerCharacterCards().forEach(cardDto -> {
                // creates an image view for the card; adjust path and size as needed
                ImageView imageView = new ImageView();
                try {
                    imageView = ImageCache.view("/assets/cards/" + cardDto.imageKey() + ".png", IMG_WIDTH, IMG_HEIGHT);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error loading image for card " + cardDto.cardId() + ": " + e.getMessage());
                }
                configureCardNode(imageView, cardDto.cardId(), false);
                lowerRow.getChildren().add(imageView);
            });
            board.lowerEventCards().forEach(cardDto -> {
                // creates an image view for the card; adjust path and size as needed
                ImageView imageView = new ImageView();
                try {
                    imageView = ImageCache.view("/assets/cards/" + cardDto.imageKey() + ".png", IMG_WIDTH, IMG_HEIGHT);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error loading image for card " + cardDto.cardId() + ": " + e.getMessage());
                }
                //configureCardNode(imageView, cardDto.cardId(), false);
                lowerRow.getChildren().add(imageView);
            });
            board.lowerBuildingCards().forEach(cardDto -> {
                // creates an image view for the card; adjust path and size as needed
                ImageView imageView = new ImageView();
                try {
                    imageView = ImageCache.view("/assets/cards/" + cardDto.imageKey() + ".png", IMG_WIDTH, IMG_HEIGHT);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error loading image for card " + cardDto.cardId() + ": " + e.getMessage());
                }
                configureCardNode(imageView, cardDto.cardId(), false);
                lowerRow.getChildren().add(imageView);
            });

        });
    }

    public void showGameStarted(int gameId, List<String> playerNicknames) {
        Platform.runLater(() -> {
            gameInfoLabel.setText("Game started. ID: " + gameId + " | Players: " + String.join(", ", playerNicknames));
            turnLabel.setText("Turn: waiting...");
            eraLabel.setText("Era: -");
        });
    }

    public void showBoard(BoardDTO board) {
        populateBoard(board);
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
            playersListView.refresh();
        });
    }

    public void clearDecks() {
        Platform.runLater(() -> {
            playerDecks.clear();
            playersListView.refresh();
        });
    }


    private String formatPlayerInfo(PlayerInfoDTO p) {
        String me = (myNickname != null && p.nickname().equals(myNickname)) ? " (you)" : "";
        return p.nickname() + me +
                " | food=" + p.food() +
                " | pp=" + p.pp() +
                " | totem=" + p.totem();
    }

    private String formatDeck(PlayerDeckDTO deck) {
        if (deck == null) return "<no deck>";

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