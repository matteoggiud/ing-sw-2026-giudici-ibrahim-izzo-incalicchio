package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.core.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * OfferTrack represents the track of OfferBoxes where players
 * can place their totems to choose cards.
 * It must contain at least 4 OfferBoxes. Players can only place their totems on free OfferBoxes,
 * and the track provides methods to get the available boxes and to place totems.
 */

public class OfferTrack {
    private final List<OfferBox> boxes;

    /**
     *
     * @param boxes the list of OfferBoxes to initialize the track with.
     *              Must contain at least 4 boxes.
     */
    public OfferTrack(List<OfferBox> boxes) {
        if (boxes.size()<4) {
            throw new IllegalArgumentException("OfferTrack must have at least 4 OfferBox");
        }
        this.boxes = new ArrayList<>(boxes);
    }

    /**
     *
     * @return an unmodifiable list of all the OfferBoxes in the track.
     */

    public List<OfferBox> getBoxes() {
        return Collections.unmodifiableList(boxes);
    }

    /**
     *
     * @return a list of the OfferBoxes that are currently free
     * (not occupied by any player's totem).
     */

    public List<OfferBox> getAvailableBoxes() {
        return boxes.stream().filter(OfferBox::isFree).toList();
    }

    /**
     *
     * @param player the player who wants to place a totem on the track.
     * @param box the OfferBox where the player wants to place their totem.
     * @throws IllegalArgumentException if the box is not part of the track or if it is already occupied by another player's totem
     * @throws IllegalStateException if the player has already placed a totem on the track
     */

    public void placeTotem(Player player, OfferBox box) {
        if (!boxes.contains(box)) throw new IllegalArgumentException("Box not in the track");
        if (!box.isFree()) throw new IllegalArgumentException("Box is already occupied");
        if (boxes.stream().anyMatch(b -> b.getPlayer() != null && b.getPlayer().equals(player))) {
            throw new IllegalStateException("Player has already placed a totem on the track");
        }
        box.placePlayer(player);
    }
}
