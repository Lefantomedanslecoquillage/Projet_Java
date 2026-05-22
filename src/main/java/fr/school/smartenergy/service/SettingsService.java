package fr.school.smartenergy.service;

public class SettingsService {

    private boolean darkMode = false;

    public void toggleDarkMode() {
        darkMode = !darkMode;
    }

    public boolean isDarkMode() {
        return darkMode;
    }
}
