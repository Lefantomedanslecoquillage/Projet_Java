package fr.school.smartenergy.model;

public class Alert {

    public enum Level {
        INFO, WARNING, DANGER
    }

    private String message;
    private Level level;

    public Alert(String message, Level level) {
        this.message = message;
        this.level = level;
    }

    public String getMessage() { return message; }
    public Level getLevel() { return level; }

    @Override
    public String toString() {
        return "[" + level + "] " + message;
    }
}
