package fr.school.smartenergy.dao;

import fr.school.smartenergy.exception.DatabaseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Manages the SQLite database connection.
 * Uses a single shared connection (singleton) for the application lifetime.
 */
public class DatabaseManager {

    private static final String DB_DIR = "data";
    private static final String DB_FILE = "smart_energy.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + "/" + DB_FILE;

    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        initDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initDatabase() {
        try {
            // Ensure the data directory exists
            Path dataDir = Paths.get(DB_DIR);
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }

            connection = DriverManager.getConnection(DB_URL);
            // Enable foreign key support
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            createTablesFromSchema();
        } catch (SQLException | IOException e) {
            throw new DatabaseException("Impossible d'initialiser la base de données", e);
        }
    }

    private void createTablesFromSchema() throws SQLException {
        String schema = loadResource("/database/schema.sql");
        if (schema == null || schema.isBlank()) return;
        try (Statement stmt = connection.createStatement()) {
            // Execute each statement separated by semicolon
            for (String sql : schema.split(";")) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        }
    }

    /** Loads a classpath resource as a String. */
    public static String loadResource(String path) {
        try (InputStream is = DatabaseManager.class.getResourceAsStream(path)) {
            if (is == null) return null;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            return null;
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Ignore on close
            }
        }
    }
}

