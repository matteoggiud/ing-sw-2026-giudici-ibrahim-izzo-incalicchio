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
import it.polimi.ingsw.server.model.observer.dto.TurnOrderSlotDTO;
import javafx.geometry.Point2D;
import java.util.Comparator;

import java.util.*;

public class GameController {

    private GuiContext guiContext;

    private static final int IMG_WIDTH = 100;
    private static final int IMG_HEIGHT = 160;
    private static final int TOTEM_SIZE = 28;
    private static final int TURN_IMG_WIDTH = 220;
    private static final int TURN_IMG_HEIGHT = 160;


    @FXML
    private Label eraLabel;
    @FXML
    private Label turnLabel;
    @FXML
    private Label gameInfoLabel;
    @FXML
    private ListView<PlayerInfoDTO> playersListView;

    @FXML
    private HBox upperRow;
    @FXML
    private HBox offerTrack;
    @FXML
    private HBox turnOrderBox;
    @FXML
    private HBox lowerRow;

    private final ObservableList<PlayerInfoDTO> playersItems = FXCollections.observableArrayList();

    // it maps nickname with his player deck
    private final Map<String, PlayerDeckDTO> playerDecks = new HashMap<>();

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
            if (guiContext != null && guiContext.clientController() != null /*TODO: check if is the player turn and is the draw action*/) {
                guiContext.clientController().drawCard(cardId, isUpper);
            }
        });
    }

    private StackPane createOfferBoxNode(int boxIndex, String imageKey, String placedPlayerNickname) {
        String img = "/assets/offer/" + imageKey + ".png";
        ImageView boxView;
        try {
            boxView = ImageCache.view(img, IMG_WIDTH, IMG_HEIGHT);
        } catch (IllegalArgumentException e) {
            // if image not found, create an empty placeholder
            boxView = new ImageView();
            boxView.setFitWidth(IMG_WIDTH);
            boxView.setFitHeight(IMG_HEIGHT);
        }

        StackPane stack = new StackPane(boxView);

        //  show totem when box has a player
        if (placedPlayerNickname != null) {
            // return player's totem from playersItems
            String totemKey = getTotemKeyForPlayer(placedPlayerNickname);

            // import and show totem's picture
            String totemPath = "/assets/totem/TOTEM_" + totemKey + ".png";
            try {
                ImageView totemView = ImageCache.view(totemPath, TOTEM_SIZE, TOTEM_SIZE);
                StackPane.setAlignment(totemView, Pos.TOP_RIGHT);
                // corner
                totemView.setTranslateX(-6);
                totemView.setTranslateY(6);
                stack.getChildren().add(totemView);
            } catch (IllegalArgumentException e) {
                System.err.println("Errore caricamento totem per " + placedPlayerNickname + ": " + e.getMessage());
            }
        }

        stack.setOnMouseClicked(e -> {
            if (guiContext != null && guiContext.clientController() != null) {
                guiContext.clientController().placeTotem(boxIndex);
            }
        });
        return stack;
    }

    // given a player nickname, return the totem key (lowercase) from playersItems; if not found, throw an exception
    private String getTotemKeyForPlayer(String nickname) {
        return playersItems.stream()
                .filter(p -> p.nickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player non trovato: " + nickname))
                .totem()
                .toUpperCase();
    }
    // given a list of TurnOrderSlotDTO, create a StackPane with the turn order image and totems of players in the correct positions
    private StackPane createTurnBox(List<TurnOrderSlotDTO> slots) {
        int numSlots = slots.size();

        String img = "/assets/turn/TURN_" + numSlots + ".png";
        ImageView boardView = ImageCache.view(img, TURN_IMG_WIDTH, TURN_IMG_HEIGHT);

        StackPane stack = new StackPane(boardView);

        List<TurnOrderSlotDTO> ordered = slots.stream()
                .sorted(Comparator.comparingInt(TurnOrderSlotDTO::position))
                .toList();

        for (TurnOrderSlotDTO slot : ordered) {
            if (slot.placedPlayerNickname() == null) {
                continue;
            }

            String totemKey = getTotemKeyForPlayer(slot.placedPlayerNickname());
            ImageView totemView = ImageCache.view("/assets/totem/TOTEM_" + totemKey + ".png", TOTEM_SIZE, TOTEM_SIZE);

            Point2D anchor = getTurnSlotAnchor(numSlots, slot.position());

            StackPane.setAlignment(totemView, Pos.CENTER);
            totemView.setTranslateX((anchor.getX() - 0.5) * TURN_IMG_WIDTH);
            totemView.setTranslateY((anchor.getY() - 0.5) * TURN_IMG_HEIGHT);

            stack.getChildren().add(totemView);
        }

        return stack;
    }
// returns the anchor point for a totem in the turn order image based on the number of slots and the slot position; coordinates are normalized (0 to 1) relative to the image size
    private Point2D getTurnSlotAnchor(int numSlots, int slotPosition) {
        // these coordinates are normalized (0 to 1) relative to the turn order image; adjust as needed based on the actual image design
        return switch (numSlots) {
            case 2 -> switch (slotPosition) {
                case 0 -> new Point2D(0.32, 0.50);
                case 1 -> new Point2D(0.68, 0.50);
                default -> throw new IllegalArgumentException("slotPosition non valida: " + slotPosition);
            };
            case 3 -> switch (slotPosition) {
                case 0 -> new Point2D(0.22, 0.50);
                case 1 -> new Point2D(0.50, 0.50);
                case 2 -> new Point2D(0.78, 0.50);
                default -> throw new IllegalArgumentException("slotPosition non valida: " + slotPosition);
            };
            case 4 -> switch (slotPosition) {
                case 0 -> new Point2D(0.16, 0.50);
                case 1 -> new Point2D(0.39, 0.50);
                case 2 -> new Point2D(0.61, 0.50);
                case 3 -> new Point2D(0.84, 0.50);
                default -> throw new IllegalArgumentException("slotPosition non valida: " + slotPosition);
            };
            case 5 -> switch (slotPosition) {
                case 0 -> new Point2D(0.10, 0.50);
                case 1 -> new Point2D(0.30, 0.50);
                case 2 -> new Point2D(0.50, 0.50);
                case 3 -> new Point2D(0.70, 0.50);
                case 4 -> new Point2D(0.90, 0.50);
                default -> throw new IllegalArgumentException("slotPosition non valida: " + slotPosition);
            };
            default -> throw new IllegalArgumentException("Numero slot non supportato: " + numSlots);
        };
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


            // OFFER TRACK: iterate on all offer boxes and show offerboxes with totem
            offerTrack.getChildren().clear();
            board.offerTrackBoxes().forEach(boxDto -> {
                StackPane boxNode = createOfferBoxNode(boxDto.position(), boxDto.imageKey(), boxDto.placedPlayerNickname());
                offerTrack.getChildren().add(boxNode);
            });

// TURN ORDER: create the turn order box with totems in the correct positions
            turnOrderBox.getChildren().clear();
            StackPane slotNode = createTurnBox(board.turnOrderSlots());
            turnOrderBox.getChildren().add(slotNode);



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
        String me = (this.guiContext.clientState().getNickname() != null && p.nickname().equals(this.guiContext.clientState().getNickname())) ? " (you)" : "";
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
                nicknameLabel.setText(item.nickname() + ((guiContext.clientState().getNickname() != null && item.nickname().equals(guiContext.clientState().getNickname())) ? " (you)" : ""));
                statsLabel.setText("food=" + item.food() + " | pp=" + item.pp() + " | totem=" + item.totem());
                PlayerDeckDTO deck = playerDecks.get(item.nickname());
                deckLabel.setText(formatDeck(deck));
                setGraphic(root);
            }
        }
    }
}