package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.board.OFFER_BOX_ID;
import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.card.Era;
import it.polimi.ingsw.model.card.characters.ArtistCharacterCard;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.ruleset.StandardRuleSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionResolverTest {

    @Test
    void resolveRegularActionThrowsWhenItIsNotPlayersTurn() {
        Game game = createGameWithPlayers(2);
        Player currentPlayer = game.getPlayers().getHost();
        Player otherPlayer = game.getPlayers().getShuffledPlayers().stream()
                .filter(p -> p != currentPlayer)
                .findFirst()
                .orElseThrow();

        OfferBox boxC = getBox(game, OFFER_BOX_ID.C);
        game.getBoard().getOfferTrack().placeTotem(currentPlayer, boxC);

        ActionResolver resolver = new ActionResolver();

        assertThrows(IllegalStateException.class,
                () -> resolver.resolveRegularAction(game, otherPlayer, null, true));
    }

    @Test
    void resolveRegularActionUpperPickCompletesActionAndMovesPlayer() {
        Game game = createGameWithPlayers(2);
        Player player = game.getPlayers().getHost();

        OfferBox boxC = getBox(game, OFFER_BOX_ID.C); // upper picks: 1, lower picks: 0
        game.getBoard().getOfferTrack().placeTotem(player, boxC);

        ArtistCharacterCard card = new ArtistCharacterCard("ArtistOne", Era.ERA_1, 2);
        game.getBoard().getUpperRow().addCard(card);

        ActionResolver resolver = new ActionResolver();
        resolver.resolveRegularAction(game, player, card, true);

        assertTrue(boxC.isFree(), "Offer box must be free after action completion");
        assertNotNull(game.getBoard().getTurnOrderBox().getSlotByPlayer(player), "Player must be moved to turn order box");
        assertFalse(game.getBoard().getUpperRow().getTribeRow().getCharacterCards().contains(card), "Card must be removed from row");
        assertTrue(player.getPlayerDeck().getCharacters().contains(card), "Card must be added to player's deck");
    }

    @Test
    void resolveRegularActionThrowsWhenUpperPickLimitIsExceeded() {
        Game game = createGameWithPlayers(2);
        Player player = game.getPlayers().getHost();

        OfferBox boxE = getBox(game, OFFER_BOX_ID.E); // upper picks: 1, lower picks: 1
        game.getBoard().getOfferTrack().placeTotem(player, boxE);

        ArtistCharacterCard firstUpperCard = new ArtistCharacterCard("ArtistOne", Era.ERA_1, 2);
        ArtistCharacterCard secondUpperCard = new ArtistCharacterCard("ArtistTwo", Era.ERA_1, 2);
        game.getBoard().getUpperRow().addCard(firstUpperCard);
        game.getBoard().getUpperRow().addCard(secondUpperCard);

        ActionResolver resolver = new ActionResolver();
        resolver.resolveRegularAction(game, player, firstUpperCard, true);

        assertThrows(IllegalStateException.class,
                () -> resolver.resolveRegularAction(game, player, secondUpperCard, true));
    }

    @Test
    void resolveExtraActionThrowsWhenCardIsNotFromUpperRow() {
        Game game = createGameWithPlayers(2);
        Player player = game.getPlayers().getHost();
        ArtistCharacterCard card = new ArtistCharacterCard("ArtistOne", Era.ERA_1, 2);

        ActionResolver resolver = new ActionResolver();

        assertThrows(IllegalArgumentException.class,
                () -> resolver.resolveExtraAction(game, player, card, false));
    }

    @Test
    void resolveAutomaticSkipsHandlesFoodOnlyBox() {
        Game game = createGameWithPlayers(5);
        Player player = game.getPlayers().getHost();

        OfferBox boxA = getBox(game, OFFER_BOX_ID.A);
        game.getBoard().getOfferTrack().placeTotem(player, boxA);

        ActionResolver resolver = new ActionResolver();
        resolver.resolveAutomaticSkips(game);

        assertTrue(boxA.isFree(), "Food-only box must be freed after automatic skip resolution");
        assertNotNull(game.getBoard().getTurnOrderBox().getSlotByPlayer(player), "Player must be moved to turn order box");
        assertEquals(6, player.getFood(), "Player should receive box reward (3) + first turn-order slot bonus (3)");
    }

    @Test
    void resolveAutomaticSkipsCompletesWhenNoSelectableCardsAreAvailable() {
        Game game = createGameWithPlayers(2);
        Player player = game.getPlayers().getHost();

        OfferBox boxE = getBox(game, OFFER_BOX_ID.E); // upper picks: 1, lower picks: 1
        game.getBoard().getOfferTrack().placeTotem(player, boxE);

        ActionResolver resolver = new ActionResolver();
        resolver.resolveAutomaticSkips(game);

        assertTrue(boxE.isFree(), "Box must be free after automatic skip completes required pending picks");
        assertNotNull(game.getBoard().getTurnOrderBox().getSlotByPlayer(player), "Player must be moved to turn order box");
    }

    private Game createGameWithPlayers(int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 5) {
            throw new IllegalArgumentException("Number of players must be between 2 and 5");
        }

        Game game = new Game(new Player("P1"));
        for (int i = 2; i <= numberOfPlayers; i++) {
            game.getPlayers().addPlayer(new Player("P" + i));
        }

        StandardRuleSet ruleSet = new StandardRuleSet();
        game.getBoard().getOfferTrack().initializeOfferTrack(ruleSet.createOfferBoxes(numberOfPlayers));
        game.getBoard().getTurnOrderBox().initializeTurnOrderBox(ruleSet.createTurnOrderSlots(numberOfPlayers));

        return game;
    }

    private OfferBox getBox(Game game, OFFER_BOX_ID boxId) {
        return game.getBoard().getOfferTrack().getBoxes().stream()
                .filter(b -> b.getBoxId() == boxId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Offer box not found: " + boxId));
    }
}

