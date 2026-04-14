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

CREATE TABLE offer_box
(
    box_id          VARCHAR(2) CHECK (box_id IN ('A', 'B', 'C', 'D', 'E', 'F', 'G')) PRIMARY KEY,
    food_reward     INTEGER NOT NULL CHECK (food_reward >= 0),
    upper_row_picks INTEGER NOT NULL CHECK (upper_row_picks >= 0),
    lower_row_picks INTEGER NOT NULL CHECK (lower_row_picks >= 0)
);

CREATE TABLE turn_order_box
(
    order_box_id SERIAL PRIMARY KEY,
    num_players  INTEGER NOT NULL CHECK (num_players BETWEEN 2 AND 5)
);

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

CREATE TABLE player
(
    player_id     SERIAL PRIMARY KEY,
    nickname      VARCHAR(255) NOT NULL UNIQUE,
    password_hash CHAR(64) NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
