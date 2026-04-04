package it.polimi.ingsw.model.gamelogic;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.card.characters.BuilderCharacterCard;
import it.polimi.ingsw.model.card.characters.InventorCharacterCard;
import it.polimi.ingsw.model.card.characters.ArtistCharacterCard;
import it.polimi.ingsw.model.card.BuildingCard;
import it.polimi.ingsw.model.deck.PlayerDeck;


import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for calculating the final score of each player at the end of the game.
 *
 */

public class ScoreCalculator {


    /**
     *
     * Computes the final score for a given player at the end of the game.
     *
     * @param player
     * @param game
     * @return the final score of the player
     */
    public static int computeFinalScore(Player player, Game game) {
        int total = player.getPP();

        total += computeBuilderScore(player);
        total += computeInventorScore(player);
        total += computeArtistScore(player);
        total += computeBuildingScore(player);

        return total;
    }

    /**
     * Computes the score from the player's builder character cards.
     *
     * @param player
     * @return the total score from the player's builder character cards.
     */
    public static int computeBuilderScore(Player player) {

        return player.getPlayerDeck().getCharacters().stream()
                .filter(c -> c instanceof BuilderCharacterCard)
                .mapToInt(c -> ((BuilderCharacterCard) c).getEndGamePP())
                .sum();
    }


    /**
     * Computes the score from the player's inventor character cards.
     *
     * @param player
     * @return the total score from the player's inventor character cards.
     *
     */
    public static int computeInventorScore(Player player) {
        PlayerDeck deck = player.getPlayerDeck();

        int countInventors = (int) deck.getCharacters().stream()
                .filter(c -> c instanceof InventorCharacterCard)
                .count();

        return countInventors * deck.getDifferentInventorItem();
    }


    /**
     * Computes the score from the player's artist character cards.
     *
     * @param player
     * @return the total score from the player's artist character cards,
     * calculated by dividing the number of artist cards in the player's deck by 2
     * and multiplying the result by 10.
     */
    public static int computeArtistScore(Player player) {
        int artists = player.getPlayerDeck().getTotalArtist();

        return (artists / 2) * 10; //
    }

    public static int computeBuildingScore(Player player) {
        return player.getPlayerDeck().getBuildings().stream()
                .mapToInt(b -> {
                    int basePoints = b.getEndGamePP();
                    int bonusPoints = (b.getEffect() != null) ? b.getEffect().ApplyEffect(/*work_in_progress*/) : 0;
                    return basePoints + bonusPoints;
                })
                .sum();
    }

    // TODO: (Andre/Pula) fix this method with the new Players class
    /**
     *  Ranks the players in the game based on their final scores.
     *
     * @param game
     * @return a map of players and their corresponding final scores, sorted in descending order of scores.
     */
    public static Map<Player, Integer> rankPlayer(Game game) {
        return game.getPlayers().stream() // [cite: 431]
                .collect(Collectors.toMap(
                        p -> p,
                        p -> computeFinalScore(p, game)
                ))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    int res = e2.getValue().compareTo(e1.getValue());
                    if (res != 0) return res;
                    return e2.getKey().getFood() - e1.getKey().getFood();
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldV, newV) -> oldV,
                        LinkedHashMap::new
                ));
    }

}
