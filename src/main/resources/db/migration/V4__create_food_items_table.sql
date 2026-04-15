CREATE TABLE IF NOT EXISTS food_items (
    id BIGSERIAL PRIMARY KEY,
    meal_entry_id BIGINT NOT NULL REFERENCES meal_entries(id) ON DELETE CASCADE,
    product_name VARCHAR(255) NOT NULL,
    weight_grams DECIMAL(8,2) NOT NULL,
    calories INTEGER NOT NULL,
    protein_g DECIMAL(8,2),
    fat_g DECIMAL(8,2),
    carbs_g DECIMAL(8,2),
    source VARCHAR(30) NOT NULL DEFAULT 'MANUAL'
);

CREATE INDEX idx_food_items_meal_entry_id ON food_items(meal_entry_id);