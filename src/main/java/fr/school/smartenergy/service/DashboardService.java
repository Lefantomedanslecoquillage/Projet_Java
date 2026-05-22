package fr.school.smartenergy.service;

import fr.school.smartenergy.dao.BuildingDao;
import fr.school.smartenergy.dao.ConsumptionDao;
import fr.school.smartenergy.model.Alert;
import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.DashboardStats;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardService {

    private final ConsumptionDao consumptionDao;
    private final BuildingDao buildingDao;

    public DashboardService() {
        this.consumptionDao = new ConsumptionDao();
        this.buildingDao = new BuildingDao();
    }

    public DashboardStats computeStats() {
        LocalDate today = LocalDate.now();

        List<ConsumptionRecord> daily = consumptionDao.findByDate(today);
        List<ConsumptionRecord> monthly = consumptionDao.findByMonth(today.getYear(), today.getMonthValue());
        List<ConsumptionRecord> yearly = consumptionDao.findByYear(today.getYear());

        double dailyTotal = sumQuantity(daily);
        double monthlyTotal = sumQuantity(monthly);
        double yearlyTotal = sumQuantity(yearly);
        double totalCost = sumCost(monthly);

        String topBuilding = findTopBuilding(yearly);
        List<Alert> alerts = buildAlerts(monthlyTotal, totalCost);

        return new DashboardStats(dailyTotal, monthlyTotal, yearlyTotal, totalCost, topBuilding, alerts);
    }

    private double sumQuantity(List<ConsumptionRecord> records) {
        return records.stream().mapToDouble(ConsumptionRecord::getQuantity).sum();
    }

    private double sumCost(List<ConsumptionRecord> records) {
        return records.stream().mapToDouble(ConsumptionRecord::getEstimatedCost).sum();
    }

    private String findTopBuilding(List<ConsumptionRecord> records) {
        if (records.isEmpty()) return "Aucune donnée";

        Map<Integer, Double> byBuilding = records.stream()
            .collect(Collectors.groupingBy(ConsumptionRecord::getBuildingId,
                Collectors.summingDouble(ConsumptionRecord::getQuantity)));

        int topId = byBuilding.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(-1);

        if (topId == -1) return "Aucune donnée";

        Building building = buildingDao.findById(topId);
        return building != null ? building.getName() : "Inconnu";
    }

    private List<Alert> buildAlerts(double monthlyTotal, double totalCost) {
        List<Alert> alerts = new ArrayList<>();
        if (monthlyTotal > 1000) {
            alerts.add(new Alert("Consommation mensuelle élevée : " + String.format("%.1f", monthlyTotal) + " unités", Alert.Level.WARNING));
        }
        if (totalCost > 500) {
            alerts.add(new Alert("Coût mensuel élevé : " + String.format("%.2f €", totalCost), Alert.Level.WARNING));
        }
        if (alerts.isEmpty()) {
            alerts.add(new Alert("Consommation dans les limites normales.", Alert.Level.INFO));
        }
        return alerts;
    }
}

