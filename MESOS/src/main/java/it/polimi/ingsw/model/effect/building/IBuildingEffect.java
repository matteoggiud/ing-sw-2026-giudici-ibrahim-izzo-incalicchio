package it.polimi.ingsw.model.effect.building;

/**
 * This interface represents the effect of a building card in the game.
 * Each building card has a specific effect that can be applied during the game,
 * and this interface defines the method to apply that effect.
 */
public interface IBuildingEffect {


    /**
     * If the building modifies the input of shamanic stars calculation, it returns the number of bonus stars, otherwise it returns 0
     *
     * @return the number of bonus stars provided by the building.
     */
    default int getBonusShamanStars() {
        return 0;
    } //TODO: Understand whether to implement an IShamanStarsProvider interface for this method, so IBuildingEffect is cleaner

    //TODO: Put the methods (Optional<IEventModifier> getEventModifier()) for both modifier, end game scoring and trigger to avoid the use of instanceof
}
