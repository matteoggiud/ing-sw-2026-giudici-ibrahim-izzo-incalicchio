package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.ImageCache;
import it.polimi.ingsw.server.model.observer.dto.EventCardDTO;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class EventPopupController {

    @FXML
    private ImageView cardImageView;

    @FXML
    private Text eventTitle;

    public void populateEventCard(EventCardDTO eventCard) {
        if (eventCard == null) return;

        // titolo
        eventTitle.setText(eventCard.name() != null ? eventCard.name() : "Resolving event");

        // immagine (fallback se non c'è)
        try {
            String imagePath = "/assets/cards/" + eventCard.imageKey() + ".png";
            ImageView iv = ImageCache.view(imagePath, 320, 350);
            cardImageView.setImage(iv.getImage());
        } catch (Exception e) {
            // ignora: rimane vuoto o mostra titolo
            System.err.println("Could not load event card image: " + e.getMessage());
        }
    }
}