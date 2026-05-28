package fr.school.smartenergy.model;

public class Building {

    private int id;
    private String name;
    private BuildingType type;
    private String address;
    private double surface;
    private int occupants;

    public Building() {}

    public Building(String name, BuildingType type, String address, double surface, int occupants) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.surface = surface;
        this.occupants = occupants;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BuildingType getType() { return type; }
    public void setType(BuildingType type) { this.type = type; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getSurface() { return surface; }
    public void setSurface(double surface) { this.surface = surface; }

    public int getOccupants() { return occupants; }
    public void setOccupants(int occupants) { this.occupants = occupants; }

    /** Creates a copy of this building without an ID (for cloning). */
    public Building cloneWithoutId() {
        return new Building(
            "Copie de " + name,
            type,
            address,
            surface,
            occupants
        );
    }

    @Override
    public String toString() {
        return name + " (" + type.getLabel() + ")";
    }
}
