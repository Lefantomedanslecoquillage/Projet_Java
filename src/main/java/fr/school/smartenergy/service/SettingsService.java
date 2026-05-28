package fr.school.smartenergy.service;

import fr.school.smartenergy.util.XmlUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads and saves application settings from/to app-settings.xml.
 * Falls back to sensible defaults if the file cannot be read.
 */
public class SettingsService {

    private static final String SETTINGS_RESOURCE = "/config/app-settings.xml";
    private static final Path SETTINGS_FILE = Paths.get("data", "app-settings.xml");

    private String theme = "light";
    private String currency = "EUR";
    private String defaultCity = "Paris";

    public SettingsService() {
        load();
    }

    public void load() {
        // Prefer a user-editable file in data/; fall back to bundled resource
        if (Files.exists(SETTINGS_FILE)) {
            try (InputStream is = Files.newInputStream(SETTINGS_FILE)) {
                applyMap(XmlUtils.readElements(is));
            } catch (Exception e) {
                loadFromResource();
            }
        } else {
            loadFromResource();
        }
    }

    private void loadFromResource() {
        try (InputStream is = SettingsService.class.getResourceAsStream(SETTINGS_RESOURCE)) {
            if (is != null) {
                applyMap(XmlUtils.readElements(is));
            }
        } catch (Exception e) {
            // Keep defaults
        }
    }

    private void applyMap(Map<String, String> map) {
        if (map.containsKey("theme")) theme = map.get("theme");
        if (map.containsKey("currency")) currency = map.get("currency");
        if (map.containsKey("defaultCity")) defaultCity = map.get("defaultCity");
    }

    public void save() {
        try {
            Files.createDirectories(SETTINGS_FILE.getParent());
            Map<String, String> values = new HashMap<>();
            values.put("theme", theme);
            values.put("currency", currency);
            values.put("defaultCity", defaultCity);
            try (OutputStream os = Files.newOutputStream(SETTINGS_FILE)) {
                XmlUtils.writeElements(values, "settings", os);
            }
        } catch (Exception e) {
            throw new RuntimeException("Impossible de sauvegarder les paramètres", e);
        }
    }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDefaultCity() { return defaultCity; }
    public void setDefaultCity(String defaultCity) { this.defaultCity = defaultCity; }
}

