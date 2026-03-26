package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OfferTrack {
    private final List<OfferBox> boxes;
    public OfferTrack(List<OfferBox> boxes) {
        if (boxes.size()<4) {
            throw new IllegalArgumentException("OfferTrack must have at least 4 OfferBox");
        }
        this.boxes = new ArrayList<>(boxes);
    }

    public List<OfferBox> getBoxes() {
        return Collections.unmodifiableList(boxes);
    }

    public List<OfferBox> getAvailableBoxes() {
        return boxes.stream().filter(OfferBox::isFree).toList();
    }

    public void placeTotem(Player player, OfferBox box) {
        if (!boxes.contains(box)) throw new IllegalArgumentException("Box not in the track");
        box.placePlayer(player);
    }
}
