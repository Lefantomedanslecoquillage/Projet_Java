package fr.school.smartenergy.model;

public class Building {
    private int id;
    private String name;
    private String address;
    private BuildingType type;
    private double surface;

    public Building(){

    }

    public Building(int id, String name, String address, BuildingType type, double surface){
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.surface = surface;
    }

    public Building cloneBuilding(){
        return new Building(0, this.name + " - copie", this.address, this.type, this.surface);
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    public BuildingType getType() {return type;}
    public void setType(BuildingType type) {this.type = type;}
    public double getSurface() {return surface;}
    public void setSurface(double surface) {this.surface = surface;}

}
