CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS teams (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    short_name VARCHAR(10),
    squad_market_value VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS positions (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS players (
    id INTEGER PRIMARY KEY,
    team_id INTEGER,
    position_id INTEGER,
    name VARCHAR(100),
    nationality VARCHAR(100),
    date_of_birth VARCHAR(20),
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (position_id) REFERENCES positions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS lineups (
    id INTEGER PRIMARY KEY,
    user_id INTEGER,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS lineups_players (
    lineup_id INTEGER,
    player_id INTEGER,
    position_id INTEGER,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (lineup_id, player_id),
    FOREIGN KEY (lineup_id) REFERENCES lineups(id),
    FOREIGN KEY (player_id) REFERENCES players(id),
    FOREIGN KEY (position_id) REFERENCES positions(id)
);

CREATE TABLE IF NOT EXISTS lineups_likes (
    user_id INTEGER,
    lineup_id INTEGER,
    created_at DATETIME,
    PRIMARY KEY (user_id, lineup_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (lineup_id) REFERENCES lineups(id)
);

CREATE TABLE IF NOT EXISTS lineups_comments (
    id INTEGER PRIMARY KEY,
    user_id INTEGER,
    lineup_id INTEGER,
    body TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (lineup_id) REFERENCES lineups(id)
);