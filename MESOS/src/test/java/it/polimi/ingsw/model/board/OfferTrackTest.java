package it.polimi.ingsw.model.board;

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
    private OfferBox box1;
    private OfferBox box2;
    private OfferBox box3;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        offerTrack = new OfferTrack();
        box1 = new OfferBox(3, 0, 0);
        box2 = new OfferBox(0, 1, 2);
        box3 = new OfferBox(0, 1, 1);
        player1 = new Player("Player1");
        player2 = new Player("Player2");

        offerTrack.initializeOfferTrack(Arrays.asList(box1, box2, box3));
    }

    @Test
    void initializeOfferTrack() {
        OfferTrack track = new OfferTrack();

        assertThrows(IllegalArgumentException.class, () -> track.initializeOfferTrack(null));
        assertThrows(IllegalArgumentException.class, () -> track.initializeOfferTrack(new ArrayList<>()));
        assertThrows(IllegalArgumentException.class, () -> track.initializeOfferTrack(Arrays.asList(box1, null)));

        List<OfferBox> firstInit = Arrays.asList(box1, box2);
        track.initializeOfferTrack(firstInit);
        assertEquals(2, track.getBoxes().size());

        List<OfferBox> secondInit = Arrays.asList(box3);
        track.initializeOfferTrack(secondInit);
        assertEquals(1, track.getBoxes().size());
        assertSame(box3, track.getBoxes().get(0));
    }

    @Test
    void getBoxByPlayer() {
        offerTrack.placeTotem(player1, box2);

        OfferBox found = offerTrack.getBoxByPlayer(player1);
        assertSame(box2, found);

        assertThrows(IllegalArgumentException.class, () -> offerTrack.getBoxByPlayer(player2));
    }

    @Test
    void getBoxByPosition() {
        assertSame(box1, offerTrack.getBoxByPosition(1));
        assertSame(box2, offerTrack.getBoxByPosition(2));
        assertSame(box3, offerTrack.getBoxByPosition(3));

        assertThrows(IllegalArgumentException.class, () -> offerTrack.getBoxByPosition(0));
        assertThrows(IllegalArgumentException.class, () -> offerTrack.getBoxByPosition(4));
    }

    @Test
    void getBoxes() {
        ArrayList<OfferBox> boxes = offerTrack.getBoxes();

        assertEquals(3, boxes.size());
        assertTrue(boxes.contains(box1));
        assertTrue(boxes.contains(box2));
        assertTrue(boxes.contains(box3));

        boxes.clear();
        assertEquals(3, offerTrack.getBoxes().size());
    }

    @Test
    void getAvailableBoxes() {
        assertEquals(3, offerTrack.getAvailableBoxes().size());

        offerTrack.placeTotem(player1, box1);
        ArrayList<OfferBox> available = offerTrack.getAvailableBoxes();

        assertEquals(2, available.size());
        assertFalse(available.contains(box1));
        assertTrue(available.contains(box2));
        assertTrue(available.contains(box3));
    }

    @Test
    void placeTotem() {
        OfferBox externalBox = new OfferBox(0, 1, 1);

        assertThrows(IllegalArgumentException.class, () -> offerTrack.placeTotem(null, box1));
        assertThrows(IllegalArgumentException.class, () -> offerTrack.placeTotem(player1, null));
        assertThrows(IllegalArgumentException.class, () -> offerTrack.placeTotem(player1, externalBox));

        offerTrack.placeTotem(player1, box1);
        assertSame(player1, box1.getPlacedPlayer());

        assertThrows(IllegalStateException.class, () -> offerTrack.placeTotem(player1, box2));
        assertThrows(IllegalStateException.class, () -> offerTrack.placeTotem(player2, box1));
    }

    @Test
    void getCurrentPlayer() {
        OfferTrack emptyTrack = new OfferTrack();
        emptyTrack.initializeOfferTrack(Arrays.asList(
                new OfferBox(0, 1, 1),
                new OfferBox(0, 1, 1)
        ));

        assertThrows(IllegalStateException.class, emptyTrack::getCurrentPlayer);

        offerTrack.placeTotem(player1, box2);
        offerTrack.placeTotem(player2, box3);

        assertSame(player1, offerTrack.getCurrentPlayer());
    }
}
