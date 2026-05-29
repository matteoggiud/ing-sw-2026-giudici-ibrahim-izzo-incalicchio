package it.polimi.ingsw.server.model.effect.building.shamanicstars;

/**
 * This interface represents a provider of shamanic stars bonus.
 * It allows implementing classes to provide a bonus
 * to the number of shamanic stars a player has during the Shamanic Ritual Event.
 */
public interface IShamanicStarsProvider {
    int getBonusShamanStars();
}
