-- Sample data for development/testing
INSERT OR IGNORE INTO buildings (id, name, type, address, surface, occupants)
VALUES
    (1, 'Maison Dupont', 'MAISON', '12 rue des Lilas, Paris', 120.0, 4),
    (2, 'Appartement Centre', 'APPARTEMENT', '5 avenue de la République, Lyon', 65.0, 2),
    (3, 'Bureau Principal', 'BUREAU', '8 rue du Commerce, Marseille', 250.0, 20);
