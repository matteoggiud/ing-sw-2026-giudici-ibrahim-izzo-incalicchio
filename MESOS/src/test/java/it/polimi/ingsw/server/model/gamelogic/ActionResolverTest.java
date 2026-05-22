package it.polimi.ingsw.server.model.gamelogic;

import it.polimi.ingsw.server.model.card.characters.ArtistCharacterCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.board.CardRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 Tests for ActionResolver main flows:
 - canResolveExtraAction
 - resolveExtraAction (happy path + error on fromUpperRow false)
 - resolveRegularAction for food-only OfferBox
 - resolveAutomaticSkips when no selectable cards exist
*/
class ActionResolverTest {

    private Game game;
    private Player host;
    private Player player;
    private StandardRuleSet ruleSet;

    @BeforeEach
    void setUp() {
        host = new Player("Host");
        // create a game with expected 2 players
        game = new Game(host, 1, 2);
        player = new Player("P1");
        game.getPlayers().addPlayer(player);

        ruleSet = new StandardRuleSet();

        // initialize turn order slots (empty)
        List<TurnOrderSlot> slots = ruleSet.createTurnOrderSlots(game.getNumberOfPlayers());
        game.getBoard().getTurnOrderBox().initializeTurnOrderBox(slots);
    }

    @Test
    void canResolveExtraAction() {
        // arrange: put a selectable character in the upper row
        ArtistCharacterCard artist = new ArtistCharacterCard(100, "Artist", new Era1(), 2, "img");
        game.getBoard().getUpperRow().addCard(artist);

        ActionResolver resolver = new ActionResolver();

        // act & assert: should be able to resolve extra action for player when upper row has selectable
        assertTrue(resolver.canResolveExtraAction(game, player));

        // null arguments -> IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> resolver.canResolveExtraAction(null, player));
        assertThrows(IllegalArgumentException.class, () -> resolver.canResolveExtraAction(game, null));
    }

    @Test
    void resolveExtraAction_happyPathAndValidation() {
        // arrange: put a selectable character in the upper row
        ArtistCharacterCard artist = new ArtistCharacterCard(101, "Artist2", new Era1(), 2, "img");
        CardRow upper = game.getBoard().getUpperRow();
        upper.addCard(artist);

        ActionResolver resolver = new ActionResolver();

        // pre-conditions
        assertTrue(upper.getTribeRow().getCharacterCards().contains(artist));
        PlayerDeck deck = player.getPlayerDeck();
        assertFalse(deck.getCharacters().contains(artist));

        // act: resolve extra action from upper row
        resolver.resolveExtraAction(game, player, artist, true);

        // assert: card removed from upper row and added to player's deck
        assertFalse(upper.getTribeRow().getCharacterCards().contains(artist));
        assertTrue(deck.getCharacters().contains(artist));

        // invalid: fromUpperRow == false should throw IllegalArgumentException
        ArtistCharacterCard another = new ArtistCharacterCard(102, "Artist3", new Era1(), 2, "img");
        game.getBoard().getUpperRow().addCard(another);
        assertThrows(IllegalArgumentException.class, () -> resolver.resolveExtraAction(game, player, another, false));
    }

    @Test
    void resolveRegularAction_foodOnlyBox_placesPlayerAndGivesFood() {
        // arrange: create a food-only box as first box in the offer track
        OfferBox foodBox = new OfferBox(3, 0, 0); // food-only
        OfferBox other = new OfferBox(0, 1, 0);
        game.getBoard().getOfferTrack().initializeOfferTrack(Arrays.asList(foodBox, other));

        // place player's totem on the first (food) box so they're the current player
        game.getBoard().getOfferTrack().placeTotem(player, foodBox);
        assertEquals(player, game.getBoard().getOfferTrack().getCurrentPlayer());

        ActionResolver resolver = new ActionResolver();

        // pre: player food is 0
        assertEquals(0, player.getFood());

        // act: resolve regular action (food-only), selectedCard must be null
        resolver.resolveRegularAction(game, player, null, false);

        // after resolution: player should have received box food (3) plus turn-order slot food bonus
        // turn order slots created by StandardRuleSet for 2 players: [1, -1] -> first free slot has food bonus 1
        int expected = 3 + 1;
        assertEquals(expected, player.getFood());

        // the box should now be free (player removed)
        assertTrue(foodBox.isFree());

        // the player should be placed in a TurnOrderSlot (not free)
        assertFalse(game.getBoard().getTurnOrderBox().isCompletelyEmpty());
    }

    @Test
    void resolveAutomaticSkips_marksPicksAndCompletesAction() {
        // arrange: offer box with 1 upper pick but no selectable cards in the upper row for player
        OfferBox box = new OfferBox(0, 1, 0);
        OfferBox other = new OfferBox(0, 1, 0);
        game.getBoard().getOfferTrack().initializeOfferTrack(Arrays.asList(box, other));

        // ensure upper row is empty (no selectable cards)
        assertFalse(game.getBoard().getUpperRow().hasSelectableCardFor(player));

        // place player's totem on the first box -> becomes current player
        game.getBoard().getOfferTrack().placeTotem(player, box);

        ActionResolver resolver = new ActionResolver();

        // act: automatic skips should detect no selectable cards, mark pending picks resolved and complete action
        resolver.resolveAutomaticSkips(game);

        // assert: the offer box should be free (player removed)
        assertTrue(box.isFree());

        // assert: player has been placed in a TurnOrderSlot (turn order box not completely empty)
        assertFalse(game.getBoard().getTurnOrderBox().isCompletelyEmpty());
    }
}
