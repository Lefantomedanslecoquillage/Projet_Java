CREATE TABLE IF NOT EXISTS buildings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    address TEXT,
    surface REAL NOT NULL,
    occupants INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS consumption_records (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    building_id INTEGER NOT NULL,
    date TEXT NOT NULL,
    time TEXT NOT NULL,
    energy_type TEXT NOT NULL,
    quantity REAL NOT NULL,
    estimated_cost REAL NOT NULL,
    FOREIGN KEY (building_id) REFERENCES buildings(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_records_building ON consumption_records(building_id);
CREATE INDEX IF NOT EXISTS idx_records_date ON consumption_records(date);
