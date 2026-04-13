package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.core.Player;

import java.util.ArrayList;
import java.util.List;

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
     * Initializes the OfferTrack with a given list of OfferBoxes.
     *
     * @param offerBoxes the list of OfferBoxes to initialize the track with.
      * @throws IllegalArgumentException if the offerBoxes list is null or empty.
     */
    public void initializeOfferTrack(List<OfferBox> offerBoxes) {
        if (offerBoxes == null || offerBoxes.contains(null) || offerBoxes.isEmpty()) {
            throw new IllegalArgumentException("offerBoxes cannot be null or empty");
        }

        this.boxes.clear();
        this.boxes.addAll(offerBoxes);
    }

    /**
     * Gets the OfferBox where a specific player has placed their totem.
     *
     * @param player the player whose OfferBox is to be retrieved.
     * @return the OfferBox where the specified player has placed their totem.
     */
    public OfferBox getBoxByPlayer(Player player) {
        for (OfferBox box : boxes) {
            if (!box.isFree() && box.getPlacedPlayer().equals(player)) {
                return box;
            }
        }
        throw new IllegalArgumentException("Offer not found");
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
