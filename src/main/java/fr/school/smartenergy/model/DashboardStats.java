package fr.school.smartenergy.model;

public class DashboardStats {

    private double dailyConsumption;
    private double monthlyConsumption;
    private double yearlyConsumption;
    private double estimatedCost;
    private String mostConsumingBuilding;
    private int alertCount;

    public DashboardStats() {
    }

    public DashboardStats(double dailyConsumption, double monthlyConsumption,
                          double yearlyConsumption, double estimatedCost,
                          String mostConsumingBuilding, int alertCount) {
        this.dailyConsumption = dailyConsumption;
        this.monthlyConsumption = monthlyConsumption;
        this.yearlyConsumption = yearlyConsumption;
        this.estimatedCost = estimatedCost;
        this.mostConsumingBuilding = mostConsumingBuilding;
        this.alertCount = alertCount;
    }
}
