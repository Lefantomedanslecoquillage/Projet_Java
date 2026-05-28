package fr.school.smartenergy.model;

public enum BuildingType {
    MAISON("Maison"),
    APPARTEMENT("Appartement"),
    BUREAU("Bureau"),
    LOCAL_COMMERCIAL("Local commercial"),
    BATIMENT_UNIVERSITAIRE("Bâtiment universitaire"),
    AUTRE("Autre");

    private final String label;

    BuildingType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
