package fr.school.smartenergy.service;

import fr.school.smartenergy.exception.DatabaseException;
import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.DashboardStats;

import java.time.LocalDate;
import java.util.List;

public class DashboardService {

    private final ConsumptionService consumptionService = new ConsumptionService();
    private final BuildingService buildingService = new BuildingService();

    public DashboardStats generateStats() throws DatabaseException {
        List<ConsumptionRecord> all = consumptionService.getAll();

        double daily = totalForDate(all, LocalDate.now());
        double monthly = totalForMonth(all);
        double yearly = totalForYear(all);
        double cost = all.stream().mapToDouble(ConsumptionRecord::getEstimatedCost).sum();

        String biggest = findBiggestConsumer();

        return new DashboardStats(
                daily,
                monthly,
                yearly,
                cost,
                biggest,
                0
        );
    }

    private double totalForDate(List<ConsumptionRecord> list, LocalDate date) {
        return list.stream()
                .filter(c -> c.getDate().equals(date))
                .mapToDouble(ConsumptionRecord::getQuantity)
                .sum();
    }

    private double totalForMonth(List<ConsumptionRecord> list) {
        return list.stream()
                .filter(c -> c.getDate().getMonth() == LocalDate.now().getMonth())
                .mapToDouble(ConsumptionRecord::getQuantity)
                .sum();
    }

    private double totalForYear(List<ConsumptionRecord> list) {
        return list.stream()
                .filter(c -> c.getDate().getYear() == LocalDate.now().getYear())
                .mapToDouble(ConsumptionRecord::getQuantity)
                .sum();
    }

    private String findBiggestConsumer() throws DatabaseException {
        List<Building> buildings = buildingService.getAllBuildings();

        double max = 0;
        String result = "Aucun";

        for (Building b : buildings) {
            double total = consumptionService.totalConsumption(b.getId());
            if (total > max) {
                max = total;
                result = b.getName();
            }
        }

        return result;
    }
}