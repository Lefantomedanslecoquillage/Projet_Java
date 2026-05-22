package fr.school.smartenergy.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsumptionRecord {

    private int id;
    private int buildingId;
    private LocalDate date;
    private LocalTime time;
    private EnergyType energyType;
    private double quantity;
    private double estimatedCost;

    public ConsumptionRecord() {}

    public ConsumptionRecord(int buildingId, LocalDate date, LocalTime time,
                             EnergyType energyType, double quantity, double estimatedCost) {
        this.buildingId = buildingId;
        this.date = date;
        this.time = time;
        this.energyType = energyType;
        this.quantity = quantity;
        this.estimatedCost = estimatedCost;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBuildingId() { return buildingId; }
    public void setBuildingId(int buildingId) { this.buildingId = buildingId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public EnergyType getEnergyType() { return energyType; }
    public void setEnergyType(EnergyType energyType) { this.energyType = energyType; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }
}
