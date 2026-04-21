package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.card.characters.BuilderCharacterCard;
import it.polimi.ingsw.model.card.characters.InventorCharacterCard;
import it.polimi.ingsw.model.deck.PlayerDeck;


import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for calculating the final score of each player at the end of the game.
 *
 */

public class ScoreCalculator {

    /**
     * Calculates the final ranking of players based on their final scores at the end of the game.
     *
     * @param game the current game context
     * @return a map of player nicknames and their corresponding final scores, sorted in descending order of scores.
     */
    public static Map<String, Integer> getFinalRanking(Game game) {
        Map<String, Integer> finalRanking = new HashMap<>();

        for (Player player : game.getPlayers().getShuffledPlayers()) {
            finalRanking.put(player.getNickname(), computeFinalScore(player));
        }

        return finalRanking;
    }


    /**
     *
     * Computes the final score for a given player at the end of the game.
     *
     * @param player the player for whom the final score is being calculated
     * @return the final score of the player
     */
    public static int computeFinalScore(Player player) {
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
     * @param player the player for whom the builder score is being calculated
     * @return the total score from the player's builder character cards.
     */
    private static int computeBuilderScore(Player player) {
        return player.getPlayerDeck().getCharacters().stream()
                .mapToInt(CharacterCard::getEndGamePPBonus)
                .sum();
    }


    /**
     * Computes the score from the player's inventor character cards.
     *
     * @param player the player for whom the inventor score is being calculated
     * @return the total score from the player's inventor character cards.
     *
     */
    private static int computeInventorScore(Player player) {
        PlayerDeck deck = player.getPlayerDeck();

        int countInventors = deck.countCharactersByType(CharacterType.INVENTOR);

        return countInventors * deck.getDifferentInventorItem();
    }


    /**
     * Computes the score from the player's artist character cards.
     *
     * @param player the player for whom the artist score is being calculated
     * @return the total score from the player's artist character cards,
     * calculated by dividing the number of artist cards in the player's deck by 2
     * and multiplying the result by 10.
     */
    private static int computeArtistScore(Player player) {
        int artists = player.getPlayerDeck().countCharactersByType(CharacterType.ARTIST);

        return (artists / 2) * 10;
    }

    /**
     *
     * Computes the score from the player's building cards.
     *
     * @param player the player for whom the building score is being calculated
     * @return the total score from the player's building cards.
     */
    private static int computeBuildingScore(Player player) {
        return player.getPlayerDeck().getBuildings().stream()
                .mapToInt(b -> {
                    int basePoints = b.getEndGamePP();
                    int bonusPoints = 0;

                    if (b.getEffect().getEndGameScoring().isPresent()) {
                        bonusPoints = b.getEffect().getEndGameScoring().get().calculateEndGameBonus(player);
                    }
                    return basePoints + bonusPoints;
                })
                .sum();
    }

    /**
     * Ranks the players in the game based on their final scores.
     *
     * @param game the current game state
     * @return a map of players and their corresponding final scores, sorted in descending order of scores.
     */
    public static Map<Player, Integer> rankPlayer(Game game) {
        return game.getPlayers().getShuffledPlayers().stream()
                .collect(Collectors.toMap(
                        p -> p,
                        ScoreCalculator::computeFinalScore
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
