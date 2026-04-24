package it.polimi.ingsw.model.board;

import it.polimi.ingsw.server.model.board.OFFER_BOX_ID;
import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.board.OfferTrack;
import it.polimi.ingsw.server.model.core.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OfferTrackTest {

    private OfferTrack offerTrack;
    private OfferBox boxA;
    private OfferBox boxB;
    private OfferBox boxC;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        offerTrack = new OfferTrack();
        boxA = new OfferBox(OFFER_BOX_ID.A, 3, 2, 1);
        boxB = new OfferBox(OFFER_BOX_ID.B, 0, 1, 2);
        boxC = new OfferBox(OFFER_BOX_ID.C, 0, 1, 1);
        player1 = new Player("Player1");
        player2 = new Player("Player2");

        offerTrack.initializeOfferTrack(Arrays.asList(boxA, boxB, boxC));
    }

    @Test
    void initializeOfferTrack() {
        OfferTrack track = new OfferTrack();

        assertThrows(IllegalArgumentException.class, () -> track.initializeOfferTrack(null));
        assertThrows(IllegalArgumentException.class, () -> track.initializeOfferTrack(new ArrayList<>()));
        assertThrows(IllegalArgumentException.class, () -> track.initializeOfferTrack(Arrays.asList(boxA, null)));

        List<OfferBox> firstInit = Arrays.asList(boxA, boxB);
        track.initializeOfferTrack(firstInit);
        assertEquals(2, track.getBoxes().size());

        List<OfferBox> secondInit = Arrays.asList(boxC);
        track.initializeOfferTrack(secondInit);
        assertEquals(1, track.getBoxes().size());
        assertSame(boxC, track.getBoxes().get(0));
    }

    @Test
    void getBoxByPlayer() {
        offerTrack.placeTotem(player1, boxB);

        OfferBox found = offerTrack.getBoxByPlayer(player1);
        assertSame(boxB, found);

        assertThrows(IllegalArgumentException.class, () -> offerTrack.getBoxByPlayer(player2));
    }

    @Test
    void getBoxes() {
        ArrayList<OfferBox> boxes = offerTrack.getBoxes();

        assertEquals(3, boxes.size());
        assertTrue(boxes.contains(boxA));
        assertTrue(boxes.contains(boxB));
        assertTrue(boxes.contains(boxC));

        // Defensive copy: mutate returned list and verify track is unchanged.
        boxes.clear();
        assertEquals(3, offerTrack.getBoxes().size());
    }

    @Test
    void getAvailableBoxes() {
        assertEquals(3, offerTrack.getAvailableBoxes().size());

        offerTrack.placeTotem(player1, boxA);
        ArrayList<OfferBox> available = offerTrack.getAvailableBoxes();

        assertEquals(2, available.size());
        assertFalse(available.contains(boxA));
        assertTrue(available.contains(boxB));
        assertTrue(available.contains(boxC));
    }

    @Test
    void placeTotem() {
        OfferBox externalBox = new OfferBox(OFFER_BOX_ID.D, 0, 1, 1);

        assertThrows(IllegalArgumentException.class, () -> offerTrack.placeTotem(null, boxA));
        assertThrows(IllegalArgumentException.class, () -> offerTrack.placeTotem(player1, null));
        assertThrows(IllegalArgumentException.class, () -> offerTrack.placeTotem(player1, externalBox));

        offerTrack.placeTotem(player1, boxA);
        assertSame(player1, boxA.getPlacedPlayer());

        // Same player cannot place twice on the track.
        assertThrows(IllegalStateException.class, () -> offerTrack.placeTotem(player1, boxB));

        // Occupied box cannot accept another player.
        assertThrows(IllegalStateException.class, () -> offerTrack.placeTotem(player2, boxA));
    }

    @Test
    void getCurrentPlayer() {
        OfferTrack emptyTrack = new OfferTrack();
        emptyTrack.initializeOfferTrack(Arrays.asList(
                new OfferBox(OFFER_BOX_ID.E, 0, 1, 1),
                new OfferBox(OFFER_BOX_ID.F, 0, 1, 1)
        ));

        assertThrows(IllegalStateException.class, emptyTrack::getCurrentPlayer);

        offerTrack.placeTotem(player1, boxB);
        offerTrack.placeTotem(player2, boxC);

        assertSame(player1, offerTrack.getCurrentPlayer());
    }
}