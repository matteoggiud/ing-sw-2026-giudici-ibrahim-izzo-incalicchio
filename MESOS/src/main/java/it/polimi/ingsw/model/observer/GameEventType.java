package it.polimi.ingsw.model.observer;

public enum GameEventType {
    PLAYER_JOINED,           // A player joined the game
    PLAYER_LEFT,             // A player left the game
    GAME_STATE_CHANGED,      // The game state changed
    TOTEM_PLACED,            // A totem was placed
    ACTION_RESOLVED,         // An action was resolved
    BOARD_CHANGED,           // The board changed
    PLAYER_RESOURCES_CHANGED, // Player resources changed (food, prestige)
    ERA_CHANGED,             // The era changed
    GAME_ENDED               // The game ended
}
