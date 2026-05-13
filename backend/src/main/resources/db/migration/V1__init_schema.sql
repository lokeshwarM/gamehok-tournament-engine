-- ============================================================
-- V1__init_schema.sql
-- Initial schema for Gamehok Tournament Engine
-- Managed by Flyway — do NOT edit after deployment
-- ============================================================

-- Extension for UUID support
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================
-- USERS
-- ============================================================
CREATE SEQUENCE users_id_seq START 1 INCREMENT 50;

CREATE TABLE users
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
    uuid          UUID          NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    username      VARCHAR(50)   NOT NULL UNIQUE,
    email         VARCHAR(150)  NOT NULL UNIQUE,
    password_hash TEXT          NOT NULL,
    display_name  VARCHAR(80),
    avatar_url    VARCHAR(500),
    game_id       VARCHAR(100) UNIQUE,
    role          VARCHAR(30)   NOT NULL DEFAULT 'PLAYER',
    elo_rating    INTEGER       NOT NULL DEFAULT 1000,
    is_verified   BOOLEAN       NOT NULL DEFAULT FALSE,
    is_active     BOOLEAN       NOT NULL DEFAULT TRUE,
    is_locked     BOOLEAN       NOT NULL DEFAULT FALSE,
    country_code  VARCHAR(3),
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    created_by    VARCHAR(100),
    updated_by    VARCHAR(100),
    version       BIGINT        NOT NULL DEFAULT 0
);

CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_elo ON users (elo_rating DESC) WHERE is_active = TRUE;

-- ============================================================
-- TEAMS
-- ============================================================
CREATE SEQUENCE teams_id_seq START 1 INCREMENT 50;

CREATE TABLE teams
(
    id           BIGINT PRIMARY KEY DEFAULT nextval('teams_id_seq'),
    uuid         UUID         NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    name         VARCHAR(80)  NOT NULL UNIQUE,
    tag          VARCHAR(10)  NOT NULL UNIQUE,
    description  VARCHAR(500),
    logo_url     VARCHAR(500),
    captain_id   BIGINT       NOT NULL REFERENCES users (id),
    team_type    VARCHAR(20)  NOT NULL,
    max_size     INTEGER      NOT NULL DEFAULT 5,
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,
    country_code VARCHAR(3),
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_by   VARCHAR(100),
    updated_by   VARCHAR(100),
    version      BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_teams_captain_id ON teams (captain_id);

CREATE SEQUENCE team_members_id_seq START 1 INCREMENT 50;

CREATE TABLE team_members
(
    id             BIGINT PRIMARY KEY DEFAULT nextval('team_members_id_seq'),
    uuid           UUID        NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    team_id        BIGINT      NOT NULL REFERENCES teams (id) ON DELETE CASCADE,
    user_id        BIGINT      NOT NULL REFERENCES users (id),
    jersey_number  INTEGER,
    is_captain     BOOLEAN     NOT NULL DEFAULT FALSE,
    is_substitute  BOOLEAN     NOT NULL DEFAULT FALSE,
    is_active      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    version        BIGINT      NOT NULL DEFAULT 0,
    CONSTRAINT uk_team_members_team_user UNIQUE (team_id, user_id)
);

CREATE INDEX idx_team_members_user_id ON team_members (user_id);

-- ============================================================
-- TOURNAMENTS
-- ============================================================
CREATE SEQUENCE tournaments_id_seq START 1 INCREMENT 10;

CREATE TABLE tournaments
(
    id                  BIGINT PRIMARY KEY DEFAULT nextval('tournaments_id_seq'),
    uuid                UUID          NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    name                VARCHAR(150)  NOT NULL,
    slug                VARCHAR(200)  NOT NULL UNIQUE,
    description         TEXT,
    banner_url          VARCHAR(500),
    tournament_type     VARCHAR(30)   NOT NULL,
    status              VARCHAR(30)   NOT NULL DEFAULT 'DRAFT',
    team_type           VARCHAR(20)   NOT NULL,
    team_size           INTEGER       NOT NULL,
    min_participants    INTEGER       NOT NULL,
    max_participants    INTEGER       NOT NULL,
    registration_start  TIMESTAMPTZ   NOT NULL,
    registration_end    TIMESTAMPTZ   NOT NULL,
    check_in_start      TIMESTAMPTZ,
    check_in_end        TIMESTAMPTZ,
    start_time          TIMESTAMPTZ   NOT NULL,
    end_time            TIMESTAMPTZ,
    game_title          VARCHAR(100)  NOT NULL,
    game_mode           VARCHAR(100),
    platform            VARCHAR(50),
    region              VARCHAR(50),
    organizer_id        BIGINT        NOT NULL REFERENCES users (id),
    seeding_strategy    VARCHAR(30)   NOT NULL DEFAULT 'RANDOM',
    is_featured         BOOLEAN       NOT NULL DEFAULT FALSE,
    is_public           BOOLEAN       NOT NULL DEFAULT TRUE,
    entry_fee           NUMERIC(12, 2),
    prize_pool          NUMERIC(12, 2),
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    version             BIGINT        NOT NULL DEFAULT 0
);

CREATE INDEX idx_tournaments_status ON tournaments (status);
CREATE INDEX idx_tournaments_organizer_id ON tournaments (organizer_id);
CREATE INDEX idx_tournaments_start_time ON tournaments (start_time);
CREATE INDEX idx_tournaments_type_status ON tournaments (tournament_type, status);
CREATE INDEX idx_tournaments_featured ON tournaments (is_featured, status) WHERE is_featured = TRUE;

-- ============================================================
-- TOURNAMENT STAGES
-- ============================================================
CREATE SEQUENCE tournament_stages_id_seq START 1 INCREMENT 20;

CREATE TABLE tournament_stages
(
    id                BIGINT PRIMARY KEY DEFAULT nextval('tournament_stages_id_seq'),
    uuid              UUID        NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    tournament_id     BIGINT      NOT NULL REFERENCES tournaments (id) ON DELETE CASCADE,
    stage_name        VARCHAR(100) NOT NULL,
    stage_type        VARCHAR(30) NOT NULL,
    format            VARCHAR(30) NOT NULL,
    status            VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    stage_order       INTEGER     NOT NULL,
    participant_count INTEGER     NOT NULL,
    qualifiers_count  INTEGER,
    best_of           INTEGER     NOT NULL DEFAULT 1,
    double_elimination BOOLEAN    NOT NULL DEFAULT FALSE,
    third_place_match BOOLEAN     NOT NULL DEFAULT FALSE,
    is_completed      BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by        VARCHAR(100),
    updated_by        VARCHAR(100),
    version           BIGINT      NOT NULL DEFAULT 0
);

CREATE INDEX idx_tournament_stages_tournament_id ON tournament_stages (tournament_id);
CREATE INDEX idx_tournament_stages_order ON tournament_stages (tournament_id, stage_order);

-- ============================================================
-- TOURNAMENT PARTICIPANTS
-- ============================================================
CREATE SEQUENCE tournament_participants_id_seq START 1 INCREMENT 50;

CREATE TABLE tournament_participants
(
    id              BIGINT PRIMARY KEY DEFAULT nextval('tournament_participants_id_seq'),
    uuid            UUID        NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    tournament_id   BIGINT      NOT NULL REFERENCES tournaments (id) ON DELETE CASCADE,
    user_id         BIGINT      REFERENCES users (id),
    team_id         BIGINT      REFERENCES teams (id),
    seed_number     INTEGER,
    status          VARCHAR(30) NOT NULL DEFAULT 'REGISTERED',
    registered_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    checked_in_at   TIMESTAMPTZ,
    eliminated_at   TIMESTAMPTZ,
    final_rank      INTEGER,
    points_earned   INTEGER     NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT      NOT NULL DEFAULT 0,
    CONSTRAINT uk_participants_tournament_team UNIQUE (tournament_id, team_id),
    CONSTRAINT uk_participants_tournament_user UNIQUE (tournament_id, user_id),
    CONSTRAINT chk_participant_type CHECK (
        (user_id IS NOT NULL AND team_id IS NULL) OR
        (team_id IS NOT NULL AND user_id IS NULL)
    )
);

CREATE INDEX idx_participants_tournament_id ON tournament_participants (tournament_id);
CREATE INDEX idx_participants_status ON tournament_participants (status);

-- ============================================================
-- MATCHES
-- ============================================================
CREATE SEQUENCE matches_id_seq START 1 INCREMENT 50;

CREATE TABLE matches
(
    id                      BIGINT PRIMARY KEY DEFAULT nextval('matches_id_seq'),
    uuid                    UUID        NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    tournament_id           BIGINT      NOT NULL REFERENCES tournaments (id),
    stage_id                BIGINT      NOT NULL REFERENCES tournament_stages (id),
    round_number            INTEGER     NOT NULL,
    match_number            INTEGER,
    bracket_position        INTEGER,
    participant1_id         BIGINT      REFERENCES tournament_participants (id),
    participant2_id         BIGINT      REFERENCES tournament_participants (id),
    winner_participant_id   BIGINT      REFERENCES tournament_participants (id),
    loser_participant_id    BIGINT      REFERENCES tournament_participants (id),
    participant1_score      INTEGER,
    participant2_score      INTEGER,
    score_json              TEXT,
    status                  VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
    result_type             VARCHAR(30),
    best_of                 INTEGER     NOT NULL DEFAULT 1,
    scheduled_at            TIMESTAMPTZ,
    started_at              TIMESTAMPTZ,
    completed_at            TIMESTAMPTZ,
    next_match_id           BIGINT,
    loser_next_match_id     BIGINT,
    is_bye                  BOOLEAN     NOT NULL DEFAULT FALSE,
    notes                   VARCHAR(1000),
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by              VARCHAR(100),
    updated_by              VARCHAR(100),
    version                 BIGINT      NOT NULL DEFAULT 0
);

CREATE INDEX idx_matches_stage_id ON matches (stage_id);
CREATE INDEX idx_matches_status ON matches (status);
CREATE INDEX idx_matches_scheduled_at ON matches (scheduled_at);
CREATE INDEX idx_matches_participant1 ON matches (participant1_id);
CREATE INDEX idx_matches_participant2 ON matches (participant2_id);

-- ============================================================
-- MATCH RESULTS
-- ============================================================
CREATE SEQUENCE match_results_id_seq START 1 INCREMENT 50;

CREATE TABLE match_results
(
    id                            BIGINT PRIMARY KEY DEFAULT nextval('match_results_id_seq'),
    uuid                          UUID        NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    match_id                      BIGINT      NOT NULL REFERENCES matches (id),
    submitted_by_participant_id   BIGINT      NOT NULL REFERENCES tournament_participants (id),
    reported_winner_id            BIGINT      NOT NULL REFERENCES tournament_participants (id),
    score_json                    TEXT,
    screenshot_url                VARCHAR(500),
    submitted_at                  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    is_disputed                   BOOLEAN     NOT NULL DEFAULT FALSE,
    dispute_reason                VARCHAR(1000),
    created_at                    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by                    VARCHAR(100),
    updated_by                    VARCHAR(100),
    version                       BIGINT      NOT NULL DEFAULT 0
);

CREATE INDEX idx_match_results_match_id ON match_results (match_id);

-- ============================================================
-- MATCHMAKING QUEUE
-- ============================================================
CREATE SEQUENCE matchmaking_queue_id_seq START 1 INCREMENT 100;

CREATE TABLE matchmaking_queue
(
    id               BIGINT PRIMARY KEY DEFAULT nextval('matchmaking_queue_id_seq'),
    uuid             UUID        NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    user_id          BIGINT      REFERENCES users (id),
    team_id          BIGINT      REFERENCES teams (id),
    team_type        VARCHAR(20) NOT NULL,
    game_title       VARCHAR(100) NOT NULL,
    game_mode        VARCHAR(100),
    region           VARCHAR(50),
    elo_rating       INTEGER     NOT NULL,
    elo_window_low   INTEGER     NOT NULL,
    elo_window_high  INTEGER     NOT NULL,
    joined_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    matched_at       TIMESTAMPTZ,
    status           VARCHAR(20) NOT NULL DEFAULT 'SEARCHING',
    matched_match_id BIGINT,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    version          BIGINT      NOT NULL DEFAULT 0
);

CREATE INDEX idx_mmq_game_mode_region ON matchmaking_queue (game_title, game_mode, region);
CREATE INDEX idx_mmq_user_id ON matchmaking_queue (user_id);
CREATE INDEX idx_mmq_joined_at ON matchmaking_queue (joined_at);
CREATE INDEX idx_mmq_status ON matchmaking_queue (status);

-- ============================================================
-- LEADERBOARD
-- ============================================================
CREATE SEQUENCE leaderboard_id_seq START 1 INCREMENT 100;

CREATE TABLE leaderboard_entries
(
    id              BIGINT PRIMARY KEY DEFAULT nextval('leaderboard_id_seq'),
    uuid            UUID        NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    tournament_id   BIGINT      NOT NULL REFERENCES tournaments (id) ON DELETE CASCADE,
    stage_id        BIGINT      NOT NULL REFERENCES tournament_stages (id) ON DELETE CASCADE,
    participant_id  BIGINT      NOT NULL REFERENCES tournament_participants (id),
    rank            INTEGER,
    points          INTEGER     NOT NULL DEFAULT 0,
    wins            INTEGER     NOT NULL DEFAULT 0,
    losses          INTEGER     NOT NULL DEFAULT 0,
    draws           INTEGER     NOT NULL DEFAULT 0,
    kills           INTEGER,
    deaths          INTEGER,
    goal_difference INTEGER,
    matches_played  INTEGER     NOT NULL DEFAULT 0,
    is_qualified    BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT      NOT NULL DEFAULT 0,
    CONSTRAINT uk_leaderboard_stage_participant UNIQUE (stage_id, participant_id)
);

CREATE INDEX idx_leaderboard_stage_points ON leaderboard_entries (stage_id, points DESC);
CREATE INDEX idx_leaderboard_tournament ON leaderboard_entries (tournament_id);

-- ============================================================
-- NOTIFICATIONS
-- ============================================================
CREATE SEQUENCE notifications_id_seq START 1 INCREMENT 100;

CREATE TABLE notifications
(
    id              BIGINT PRIMARY KEY DEFAULT nextval('notifications_id_seq'),
    uuid            UUID        NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    user_id         BIGINT      NOT NULL REFERENCES users (id),
    title           VARCHAR(200) NOT NULL,
    body            TEXT        NOT NULL,
    type            VARCHAR(50) NOT NULL,
    channel         VARCHAR(20) NOT NULL,
    reference_id    BIGINT,
    reference_type  VARCHAR(50),
    is_read         BOOLEAN     NOT NULL DEFAULT FALSE,
    read_at         TIMESTAMPTZ,
    sent_at         TIMESTAMPTZ,
    delivery_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    metadata        TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT      NOT NULL DEFAULT 0
);

CREATE INDEX idx_notifications_user_id ON notifications (user_id);
CREATE INDEX idx_notifications_is_read ON notifications (user_id, is_read);
CREATE INDEX idx_notifications_created_at ON notifications (created_at DESC);

-- ============================================================
-- PRIZE ALLOCATIONS
-- ============================================================
CREATE SEQUENCE prize_allocations_id_seq START 1 INCREMENT 20;

CREATE TABLE prize_allocations
(
    id               BIGINT PRIMARY KEY DEFAULT nextval('prize_allocations_id_seq'),
    uuid             UUID           NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    tournament_id    BIGINT         NOT NULL REFERENCES tournaments (id) ON DELETE CASCADE,
    rank_placement   INTEGER        NOT NULL,
    prize_type       VARCHAR(20)    NOT NULL,
    cash_amount      NUMERIC(12, 2),
    credit_amount    INTEGER,
    item_description VARCHAR(500),
    trophy_name      VARCHAR(200),
    badge_code       VARCHAR(100),
    is_distributed   BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    version          BIGINT         NOT NULL DEFAULT 0
);

CREATE INDEX idx_prize_tournament_id ON prize_allocations (tournament_id);
CREATE INDEX idx_prize_rank ON prize_allocations (tournament_id, rank_placement);

-- ============================================================
-- PRIZE DISTRIBUTIONS
-- ============================================================
CREATE SEQUENCE prize_distributions_id_seq START 1 INCREMENT 20;

CREATE TABLE prize_distributions
(
    id               BIGINT PRIMARY KEY DEFAULT nextval('prize_distributions_id_seq'),
    uuid             UUID           NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    tournament_id    BIGINT         NOT NULL REFERENCES tournaments (id),
    participant_id   BIGINT         NOT NULL REFERENCES tournament_participants (id),
    rank_achieved    INTEGER        NOT NULL,
    prize_type       VARCHAR(20)    NOT NULL,
    cash_amount      NUMERIC(12, 2),
    description      VARCHAR(500),
    distributed_at   TIMESTAMPTZ,
    status           VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    reference_id     VARCHAR(100),
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    version          BIGINT         NOT NULL DEFAULT 0
);

CREATE INDEX idx_prize_dist_participant ON prize_distributions (participant_id);
CREATE INDEX idx_prize_dist_tournament ON prize_distributions (tournament_id);
