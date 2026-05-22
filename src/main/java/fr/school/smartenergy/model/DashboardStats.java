package fr.school.smartenergy.model;

import java.util.List;

public class DashboardStats {

    private double dailyTotal;
    private double monthlyTotal;
    private double yearlyTotal;
    private double totalCost;
    private String topBuildingName;
    private List<Alert> alerts;

    public DashboardStats() {}

    public DashboardStats(double dailyTotal, double monthlyTotal, double yearlyTotal,
                          double totalCost, String topBuildingName, List<Alert> alerts) {
        this.dailyTotal = dailyTotal;
        this.monthlyTotal = monthlyTotal;
        this.yearlyTotal = yearlyTotal;
        this.totalCost = totalCost;
        this.topBuildingName = topBuildingName;
        this.alerts = alerts;
    }

    public double getDailyTotal() { return dailyTotal; }
    public void setDailyTotal(double dailyTotal) { this.dailyTotal = dailyTotal; }

    public double getMonthlyTotal() { return monthlyTotal; }
    public void setMonthlyTotal(double monthlyTotal) { this.monthlyTotal = monthlyTotal; }

    public double getYearlyTotal() { return yearlyTotal; }
    public void setYearlyTotal(double yearlyTotal) { this.yearlyTotal = yearlyTotal; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getTopBuildingName() { return topBuildingName; }
    public void setTopBuildingName(String topBuildingName) { this.topBuildingName = topBuildingName; }

    public List<Alert> getAlerts() { return alerts; }
    public void setAlerts(List<Alert> alerts) { this.alerts = alerts; }
}
