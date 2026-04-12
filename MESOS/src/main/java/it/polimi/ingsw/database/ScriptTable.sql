CREATE TABLE CharacterCard
(
    cardId        SERIAL PRIMARY KEY,
    name          VARCHAR(16) NOT NULL,
    era           VARCHAR(4) CHECK( era IN ('I', 'II', 'III')),
    minnumplayers INTEGER CHECK (minnumplayers BETWEEN 2 AND 5),
    charactertype VARCHAR(8) CHECK ( charactertype IN (
        'HUNTER',
        'GATHERER',
        'SHAMAN',
        'ARTIST',
        'BUILDER',
        'INVENTOR'
     )),
    EffectParams  JSON
)

CREATE TABLE BuildingCard
(
    cardId          SERIAL PRIMARY KEY,
    name            VARCHAR(16),
    era             VARCHAR(3) NOT NULL CHECK (era IN ('I', 'II', 'III')),
    minNumPlayers   INTEGER NOT NULL CHECK (minNumPlayers BETWEEN 2 AND 5),
    foodCost        INTEGER NOT NULL CHECK (foodCost >= 0),
    endGamePp       INTEGER NOT NULL CHECK (endGamePp >= 0),
    IBuildingEffect VARCHAR(64) NOT NULL CHECK (IBuildingEffect IN (
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
    effectParams JSON
)

CREATE TABLE EventCard
(
    cardId        SERIAL PRIMARY KEY,
    name          VARCHAR(16) NOT NULL,
    era           ENUM('I', 'II', 'III') NOT NULL,
    minnumplayers INTEGER NOT NULL CHECK (minnumplayers BETWEEN 2 AND 5),
    isFinalEvent  BOOLEAN NOT NULL DEFAULT FALSE,
    EventType     VARCHAR(16) NOT NULL CHECK (EventType IN ('HUNT', 'GATHER', 'SHAMAN', 'ARTIST', 'BUILDER', 'INVENTOR')),
    gainedFood    INTEGER NOT NULL CHECK (gainedFood >= 0),
    malusFood     INTEGER NOT NULL CHECK (malusFood >= 0),
    gainedPp      INTEGER NOT NULL CHECK (gainedPp >= 0),
    malusPp       INTEGER NOT NULL CHECK (malusPp >= 0),
    threshold     INTEGER NOT NULL CHECK (threshold >= 0)
)

CREATE TABLE OfferBox
(
    boxId         varchar(2) CHECK (boxId IN ('A', 'B', 'C', 'D', 'E', 'F', 'G')) PRIMARY KEY,
    foodReward    INTEGER NOT NULL CHECK (foodReward >= 0),
    upperRowPicks INTEGER NOT NULL CHECK (upperRowPicks >= 0),
    lowerRowPicks INTEGER NOT NULL CHECK (lowerRowPicks >= 0)
)

CREATE TABLE TurnOrderBox
(
    orderBoxId SERIAL PRIMARY KEY,
    numPlayers INTEGER NOT NULL CHECK (numPlayers BETWEEN 2 AND 5),
)

CREATE TABLE TurnOrderSlot
(
    slotId     SERIAL PRIMARY KEY,
    orderBoxId INTEGER NOT NULL ,
    position   INTEGER CHECK (position BETWEEN 0 AND 4),
    foodCost   INTEGER NOT NULL,
    ppCost     INTEGER NOT NULL ,
    FOREIGN KEY (orderBoxId) REFERENCES TurnOrderBox (orderBoxId),
    UNIQUE (orderBoxId, position) --Per ogni Box non possono esserci due slot con la stessa posizione
)

CREATE TABLE player
(
    playerId     SERIAL PRIMARY KEY,
    nickname     VARCHAR(255) NOT NULL UNIQUE,
    email        VARCHAR(255) NOT NULL UNIQUE,
    passwordHash VARCHAR(64)  NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
)