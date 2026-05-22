package fr.school.smartenergy.dao;

import fr.school.smartenergy.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:smartenergy.db";

    static {
        try {
            initDatabase();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws DatabaseException {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new DatabaseException("Erreur connexion base de données", e);
        }
    }

    private static void initDatabase() throws DatabaseException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Table bâtiments
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS buildings (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    address TEXT,
                    type TEXT,
                    surface REAL
                )
            """);

            // Table consommations
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS consumption (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    building_id INTEGER,
                    date TEXT,
                    time TEXT,
                    energy_type TEXT,
                    quantity REAL,
                    cost REAL,
                    FOREIGN KEY(building_id) REFERENCES buildings(id)
                )
            """);

        } catch (SQLException e) {
            throw new DatabaseException("Erreur initialisation BD", e);
        }
    }
}