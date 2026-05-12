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

        // title
        eventTitle.setText(eventCard.name() != null ? eventCard.name() : "Resolving event");

        // image
        try {
            String imagePath = "/assets/cards/" + eventCard.imageKey() + ".png";
            ImageView iv = ImageCache.view(imagePath, 320, 350);
            cardImageView.setImage(iv.getImage());
        } catch (Exception e) {
            // ignore: if the image cannot be loaded, we just won't show it
            System.err.println("Could not load event card image: " + e.getMessage());
        }
    }
}