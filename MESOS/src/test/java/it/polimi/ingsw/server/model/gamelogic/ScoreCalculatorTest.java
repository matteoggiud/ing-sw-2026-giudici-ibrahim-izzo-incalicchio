package it.polimi.ingsw.server.model.gamelogic;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.characters.ArtistCharacterCard;
import it.polimi.ingsw.server.model.card.characters.BuilderCharacterCard;
import it.polimi.ingsw.server.model.card.characters.InventorCharacterCard;
import it.polimi.ingsw.server.model.card.characters.InventorType;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/*
  Tests for ScoreCalculator.
  Comments and assertions are in English.
*/
class ScoreCalculatorTest {

    @Test
    void computeFinalScore_basicCombinationOfCardsAndBuildings() {
        // Arrange
        Player p = new Player("Alice");
        // set base prestige points
        p.setPp(10);

        PlayerDeck deck = p.getPlayerDeck();

        // Add one Builder character with endGamePP = 2
        BuilderCharacterCard builder = new BuilderCharacterCard(
                1, "Builder", new Era1(), 2, 2, 0, "img");
        deck.addCharacterCard(builder);

        // Add two Artist characters (artist score = (2/2)*10 = 10)
        ArtistCharacterCard artist1 = new ArtistCharacterCard(2, "Artist1", new Era1(), 2, "img");
        ArtistCharacterCard artist2 = new ArtistCharacterCard(3, "Artist2", new Era1(), 2, "img");
        deck.addCharacterCard(artist1);
        deck.addCharacterCard(artist2);

        // Add two Inventor characters with different InventorType
        InventorCharacterCard inventor1 = new InventorCharacterCard(4, "Inv1", new Era1(), 2, InventorType.BOAT, "img");
        InventorCharacterCard inventor2 = new InventorCharacterCard(5, "Inv2", new Era1(), 2, InventorType.ARROW, "img");
        deck.addCharacterCard(inventor1);
        deck.addCharacterCard(inventor2);
        // countInventors = 2, differentInventorItem = 2 -> inventor score = 4

        // Add one building with endGamePP = 7 and no extra endGame scoring
        IBuildingEffect noEffect = new IBuildingEffect() { /* use default methods */ };
        BuildingCard building = new BuildingCard(10, "House", new Era1(), 2, 3, 7, noEffect, "img");
        deck.addBuildingCard(building);

        // Act
        int total = ScoreCalculator.computeFinalScore(p);

        // Assert
        // Expected total:
        // base PP = 10
        // builder endGamePP = 2
        // artist score = (2/2)*10 = 10
        // inventor score = 2(count)*2(different) = 4
        // building endGamePP = 7
        // Total = 10 + 2 + 10 + 4 + 7 = 33
        assertEquals(33, total, "Compute final score must sum base PP + character bonuses + building points");
    }

    @Test
    void getFinalRanking_returnsNicknamesSortedByScoreDescending() {
        // Arrange
        Player host = new Player("Host");
        Game game = new Game(host, 1); // will add host
        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        game.getPlayers().addPlayer(alice);
        game.getPlayers().addPlayer(bob);

        // Set scores via setPp (we rely on computeFinalScore which considers PP + other factors)
        host.setPp(5);
        alice.setPp(20);
        bob.setPp(15);

        // Act
        LinkedHashMap<String, Integer> ranking = ScoreCalculator.getFinalRanking(game);

        // Assert ordering: Alice (20), Bob (15), Host (5)
        String[] order = ranking.keySet().toArray(new String[0]);
        assertEquals("Alice", order[0]);
        assertEquals("Bob", order[1]);
        assertEquals("Host", order[2]);

        assertEquals(20, ranking.get("Alice"));
        assertEquals(15, ranking.get("Bob"));
        assertEquals(5, ranking.get("Host"));
    }

    @Test
    void rankPlayer_tieBreakByFoodDescending() {
        // Arrange
        Player host = new Player("H");
        Game game = new Game(host, 2);
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        game.getPlayers().addPlayer(p1);
        game.getPlayers().addPlayer(p2);

        // Create a tie in computeFinalScore: set same PP and no other cards
        host.setPp(0);
        p1.setPp(10);
        p2.setPp(10);

        // Use different food to break the tie: higher food should rank first
        p1.setFood(3);
        p2.setFood(1);

        // Act
        Map<Player, Integer> ranking = ScoreCalculator.rankPlayer(game);

        // Assert: ranking is an ordered map; first key should be p1 (same score but more food)
        Player first = ranking.keySet().iterator().next();
        assertEquals(p1, first, "When players tie on score, player with more food must be ranked higher");
    }
}
