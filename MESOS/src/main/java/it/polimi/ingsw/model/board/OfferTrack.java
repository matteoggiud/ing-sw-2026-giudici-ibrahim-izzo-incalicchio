package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.core.Player;

import java.util.ArrayList;

/**
 * OfferTrack represents the track of OfferBoxes where players can place their totems to choose cards.
 */
public class OfferTrack {
    /**
     * The list of OfferBoxes in the track.
     */
    private final ArrayList<OfferBox> boxes;


    /**
     * Constructs an empty OfferTrack.
     */
    public OfferTrack() {
        this.boxes = new ArrayList<>();
    }


    /**
     * Constructs an OfferTrack with a given list of OfferBoxes.
     *
     * @param boxes the list of OfferBoxes to initialize the track with.
     * @throws IllegalArgumentException if the list of boxes is null or contains fewer than 4 boxes.
     */
    public OfferTrack(ArrayList<OfferBox> boxes) {
        if (boxes == null || boxes.size() < 4) {
            throw new IllegalArgumentException("OfferTrack must have at least 4 OfferBox and cannot be null");
        }

        this.boxes = new ArrayList<>(boxes);
    }


    /**
     * Gets the list of OfferBoxes in the track.
     *
     * @return a list of the OfferBoxes in the track.
     */
    public ArrayList<OfferBox> getBoxes() {
        return new ArrayList<>(this.boxes);
    }

    /**
     * Gets the list of available OfferBoxes in the track, which are not occupied by any player's totem.
     *
     * @return a list of the OfferBoxes that are currently free
     */
    public ArrayList<OfferBox> getAvailableBoxes() {
        ArrayList<OfferBox> availableBoxes = new ArrayList<>();
        for (OfferBox box : boxes) {
            if (box.isFree())
                availableBoxes.add(box);
        }
        return availableBoxes;
        // return boxes.stream().filter(OfferBox::isFree).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Places a player's totem on a specified OfferBox in the track.
     *
     * @param player the player who wants to place a totem on the track.
     * @param box    the OfferBox where the player wants to place their totem.
     * @throws IllegalArgumentException if the box is not part of the track or if it is already occupied by another player's totem
     * @throws IllegalStateException    if the player has already placed a totem on the track
     */
    public void placeTotem(Player player, OfferBox box) throws IllegalArgumentException, IllegalStateException {

        if (box == null || player == null) throw new IllegalArgumentException("Arguments cannot be null");
        if (!boxes.contains(box)) throw new IllegalArgumentException("Box not in the track");

        if (boxes.stream().anyMatch(b -> b.getPlacedPlayer() != null && b.getPlacedPlayer().equals(player))) {
            throw new IllegalStateException("Player has already placed a totem on the track");
        }

        box.placePlayer(player);
    }
}
