-- SQL script to create the database tables for the game application.

-- The character_card table contains all the character cards in the game, with their name, era, minimum number of players, character type, and effect parameters.
-- The character_type field is a VARCHAR with a CHECK constraint to ensure it only contains valid character types.
-- The effect_params field is a JSON object that can store any additional parameters needed for the character's effect.
CREATE TABLE character_card
(
    card_id         SERIAL PRIMARY KEY,
    name            VARCHAR(16) NOT NULL,
    era             VARCHAR(5) CHECK (era IN ('ERA_1', 'ERA_2', 'ERA_3')),
    min_num_players INTEGER CHECK (min_num_players BETWEEN 2 AND 5),
    character_type  VARCHAR(8) CHECK (character_type IN (
            'HUNTER',
            'GATHERER',
            'SHAMAN',
            'ARTIST',
            'BUILDER',
            'INVENTOR'
        )),
    effect_params   JSON
);

-- The building_card table contains all the building cards in the game, with their name, era, minimum number of players, food cost, end game prestige points, effect type, and effect parameters.
-- The effect_id field is a VARCHAR with a CHECK constraint to ensure it only contains valid effect IDs.
-- The min_num_players is used to determine in which player count configurations the card can be included in the game setup.
CREATE TABLE building_card
(
    card_id         SERIAL PRIMARY KEY,
    name            VARCHAR(16),
    era             VARCHAR(5) NOT NULL CHECK (era IN ('ERA_1', 'ERA_2', 'ERA_3')),
    min_num_players INTEGER NOT NULL CHECK (min_num_players BETWEEN 2 AND 5),
    food_cost       INTEGER NOT NULL CHECK (food_cost >= 0),
    end_game_pp     INTEGER NOT NULL CHECK (end_game_pp >= 0),
    effect_id       VARCHAR(64) NOT NULL CHECK (effect_id IN (
        'DOUBLEBUILDERBUILDINGEFFECT',
        'ENDPPCARDSSETBUILDINGEFFECT',
        'FLATPRESTIGEPOINTBUILDINGCARD',
        'PPPERCHARACTERBUILDINGEFFECT',
        'EXTRACARDBUILDINGEFFECT',
        'HUNTBONUSBUILDINGEFFECT',
        'ROCKPAINTINGSBONUSBUILDINGEFFECT',
        'SHAMANICDOUBLEBUILDINGEFFECT',
        'SHAMANICIMMUNITYBUILDINGEFFECT',
        'SUSTENANCEDISCOUNTBUILDINGEFFECT',
        'SHAMANICSTARSBUILDINGEFFECT',
        'TOTEMBONUSBUILDINGEFFECT',
        'TRIGGERCARDSSETBUILDINGEFFECT',
        'TRIGGERINVENTORSCOUPLEBUILDINGEFFECT'
        )),
    effect_params   JSON
);

-- The event_card table contains all the event cards in the game, with their name, era, minimum number of players, whether they are final events, event type, and effect parameters.
-- The threshold field is used to determine when the event is triggered based on the current game state, and the gained/malus food and prestige points are used to apply the event's effects when it is triggered.
CREATE TABLE event_card
(
    card_id         SERIAL PRIMARY KEY,
    name            VARCHAR(16) NOT NULL,
    era             VARCHAR(5) CHECK (era IN ('ERA_1', 'ERA_2', 'ERA_3')),
    min_num_players INTEGER NOT NULL CHECK (min_num_players BETWEEN 2 AND 5),
    is_final_event  BOOLEAN NOT NULL DEFAULT FALSE,
    event_type      VARCHAR(16) NOT NULL CHECK (event_type IN ('HUNT', 'SHAMANIC_RITUAL', 'ROCK_PAINTINGS', 'SUSTENANCE')),
    gained_food     INTEGER NOT NULL CHECK (gained_food >= 0),
    malus_food      INTEGER NOT NULL CHECK (malus_food >= 0),
    gained_pp       INTEGER NOT NULL CHECK (gained_pp >= 0),
    malus_pp        INTEGER NOT NULL CHECK (malus_pp >= 0),
    threshold       INTEGER NOT NULL CHECK (threshold >= 0)
);

-- The card table is a central table that links to the character_card, building_card, and event_card tables. Each card can be of only one type (character, building, or event), and this is enforced by the CHECK constraint on the num_nonnulls function.
CREATE TABLE card(
    id                SERIAL PRIMARY KEY,
    character_card_id INTEGER,
    building_card_id  INTEGER,
    event_card_id     INTEGER,
    image_key         VARCHAR(255) NOT NULL UNIQUE,

    CONSTRAINT fk_card_character
        FOREIGN KEY (character_card_id)
            REFERENCES character_card (card_id),

    CONSTRAINT fk_card_building
        FOREIGN KEY (building_card_id)
            REFERENCES building_card (card_id),

    CONSTRAINT fk_card_event
        FOREIGN KEY (event_card_id)
            REFERENCES event_card (card_id),

    CONSTRAINT uq_card_character UNIQUE (character_card_id),

    CONSTRAINT uq_card_building UNIQUE (building_card_id),

    CONSTRAINT uq_card_event UNIQUE (event_card_id),

    CONSTRAINT ck_card_exactly_one_type
        CHECK (num_nonnulls(character_card_id, building_card_id, event_card_id) = 1)
);

-- The offer_box table contains the different offer boxes in the game, with their box ID, food reward, and the number of picks for the upper and lower rows.
CREATE TABLE offer_box
(
    box_id          VARCHAR(2) CHECK (box_id IN ('A', 'B', 'C', 'D', 'E', 'F', 'G')) PRIMARY KEY,
    food_reward     INTEGER NOT NULL CHECK (food_reward >= 0),
    upper_row_picks INTEGER NOT NULL CHECK (upper_row_picks >= 0),
    lower_row_picks INTEGER NOT NULL CHECK (lower_row_picks >= 0)
);

-- The turn_order_box and turn_order_slot tables define the turn order configurations for different player counts.
CREATE TABLE turn_order_box
(
    order_box_id SERIAL PRIMARY KEY,
    num_players  INTEGER NOT NULL CHECK (num_players BETWEEN 2 AND 5)
);

-- The turn_order_slot table contains the individual slots for each turn order box, with their position, food cost, and prestige point cost. Each slot is linked to a specific turn order box, and the combination of order_box_id and position must be unique.
CREATE TABLE turn_order_slot
(
    slot_id      SERIAL PRIMARY KEY,
    order_box_id INTEGER NOT NULL,
    position     INTEGER CHECK (position BETWEEN 0 AND 4),
    food_cost    INTEGER NOT NULL,
    pp_cost      INTEGER NOT NULL,
    FOREIGN KEY (order_box_id) REFERENCES turn_order_box (order_box_id),
    UNIQUE (order_box_id, position)
);

-- The player table contains the registered players in the game, with their player ID, nickname, password hash, and the timestamp of when they created their account.
CREATE TABLE player
(
    player_id     SERIAL PRIMARY KEY,
    nickname      VARCHAR(255) NOT NULL UNIQUE,
    password_hash CHAR(64) NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- The game table contains the active and completed games in the system, with their game ID, which is incremental and serves as the primary key
-- The parameter in_progress is a boolean that indicates whether the game is currently active or has finished
-- The game_snapshot field is a JSONB object that stores the current state of the game for the persistence mechanism
CREATE TABLE game (
    game_id    SERIAL PRIMARY KEY,
    expected_players INTEGER NOT NULL CHECK (expected_players BETWEEN 2 AND 5),
    host_id INTEGER NOT NULL REFERENCES player (player_id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP DEFAULT NULL,
    in_progress BOOLEAN NOT NULL DEFAULT FALSE,
    game_snapshot JSONB NOT NULL DEFAULT '{}'
);

-- The game_player table is a junction table that links the game and player tables, representing the players participating in each game.
-- Explicity asked to save the final score in the database
CREATE TABLE game_player (
    game_id   INTEGER NOT NULL,
    player_id INTEGER NOT NULL,
    game_ranking INTEGER DEFAULT NULL,
    final_score INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (game_id, player_id),
    FOREIGN KEY (game_id) REFERENCES game (game_id) ON DELETE CASCADE,
    FOREIGN KEY (player_id) REFERENCES player (player_id) ON DELETE CASCADE
);