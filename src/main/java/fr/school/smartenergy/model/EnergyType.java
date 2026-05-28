package fr.school.smartenergy.model;

public enum EnergyType {
    ELECTRICITY("Électricité", "kWh"),
    WATER("Eau", "m³"),
    GAS("Gaz", "m³"),
    HEATING("Chauffage", "kWh"),
    AIR_CONDITIONING("Climatisation", "kWh"),
    SOLAR_PRODUCTION("Production solaire", "kWh");

    private final String label;
    private final String unit;

    EnergyType(String label, String unit) {
        this.label = label;
        this.unit = unit;
    }

    public String getLabel() {
        return label;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return label;
    }
}
