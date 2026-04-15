CREATE TABLE IF NOT EXISTS client_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    birthday DATE,
    weight_kg DECIMAL(5,2),
    height_cm DECIMAL(5,2),
    daily_calorie_goal INTEGER,
    trust_score INTEGER DEFAULT 70
);