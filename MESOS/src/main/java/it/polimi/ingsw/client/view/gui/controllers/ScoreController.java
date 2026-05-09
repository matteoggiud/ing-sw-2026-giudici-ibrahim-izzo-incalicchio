package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.GuiContext;
import it.polimi.ingsw.server.model.observer.dto.LeadBoardDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreController {

    private GuiContext guiContext;

    @FXML
    private Label subtitleLabel;

    @FXML
    private ListView<String> rankingListView;

    private final ObservableList<String> rankingItems = FXCollections.observableArrayList();

    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }

    @FXML
    private void initialize() {
        rankingListView.setItems(rankingItems);
    }

    /**
     * Popola la classifica usando la final ranking della partita (map nickname -> score).
     * Visualizza in forma ordinata (#pos Nickname - X pt). Evidenzia "(tu)" se è il player locale.
     */
    public void showFinalRanking(Map<String, Integer> finalRanking, List<LeadBoardDTO> historical) {
        Platform.runLater(() -> {
            rankingItems.clear();

            if (finalRanking == null || finalRanking.isEmpty()) {
                // Se non c'è la mappa, ma potrebbe esserci la lista storica (non è il caso), mostriamo messaggio
                rankingItems.add("Classifica non disponibile.");
                return;
            }

            // Ordina per punteggio discendente, poi nome
            List<Map.Entry<String, Integer>> ordered = finalRanking.entrySet().stream()
                    .sorted((e1, e2) -> {
                        int cmp = Integer.compare(e2.getValue(), e1.getValue());
                        if (cmp != 0) return cmp;
                        return e1.getKey().compareToIgnoreCase(e2.getKey());
                    })
                    .collect(Collectors.toList());

            String me = guiContext != null && guiContext.clientState() != null ? guiContext.clientState().getNickname() : null;

            int pos = 1;
            for (Map.Entry<String, Integer> e : ordered) {
                String nick = e.getKey() == null ? "-" : e.getKey();
                String label = String.format("#%d %s%s \u2014 %d pt",
                        pos++,
                        nick,
                        (me != null && me.equals(nick) ? " (tu)" : ""),
                        e.getValue() == null ? 0 : e.getValue());
                rankingItems.add(label);
            }
        });
    }

    /**
     * Azione del pulsante: torna al menu. Chiede la lista giochi al server; la UI sarà aggiornata da showAvailableGames().
     */
    @FXML
    private void handleBackToMenu() {
        if (guiContext == null || guiContext.clientController() == null) return;

        // La partita è già finita -> chiediamo la lista dei giochi per tornare al menu
        guiContext.clientController().getAvailableGames();
    }
}

