package fr.school.smartenergy.service;

import fr.school.smartenergy.dao.BuildingDao;
import fr.school.smartenergy.dao.ConsumptionDao;
import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Prepares data structures suitable for JavaFX charts.
 */
public class ChartService {

    private final ConsumptionDao consumptionDao;
    private final BuildingDao buildingDao;

    public ChartService() {
        this.consumptionDao = new ConsumptionDao();
        this.buildingDao = new BuildingDao();
    }

    /**
     * Returns daily consumption totals for the last N days.
     * Result: Map of date string -> total quantity
     */
    public Map<String, Double> getDailyEvolution(int lastDays) {
        Map<String, Double> result = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        for (int i = lastDays - 1; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            List<ConsumptionRecord> records = consumptionDao.findByDate(d);
            double total = records.stream().mapToDouble(ConsumptionRecord::getQuantity).sum();
            result.put(d.toString(), total);
        }
        return result;
    }

    /**
     * Returns total consumption per building for the current year.
     * Result: Map of building name -> total quantity
     */
    public Map<String, Double> getConsumptionPerBuilding() {
        List<ConsumptionRecord> records = consumptionDao.findByYear(LocalDate.now().getYear());
        Map<Integer, Double> byId = records.stream()
            .collect(Collectors.groupingBy(ConsumptionRecord::getBuildingId,
                Collectors.summingDouble(ConsumptionRecord::getQuantity)));

        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : byId.entrySet()) {
            Building b = buildingDao.findById(entry.getKey());
            String name = b != null ? b.getName() : "Bâtiment " + entry.getKey();
            result.put(name, entry.getValue());
        }
        return result;
    }

    /**
     * Returns total consumption per energy type for the current year.
     * Result: Map of energy type label -> total quantity
     */
    public Map<String, Double> getConsumptionPerEnergyType() {
        List<ConsumptionRecord> records = consumptionDao.findByYear(LocalDate.now().getYear());
        Map<EnergyType, Double> byType = records.stream()
            .collect(Collectors.groupingBy(ConsumptionRecord::getEnergyType,
                Collectors.summingDouble(ConsumptionRecord::getQuantity)));

        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<EnergyType, Double> entry : byType.entrySet()) {
            result.put(entry.getKey().getLabel(), entry.getValue());
        }
        return result;
    }

    /**
     * Returns monthly consumption totals for all 12 months of the given year.
     * Result: Map of month number (1-12) as String -> total quantity
     */
    public Map<String, Double> getMonthlyEvolution(int year) {
        Map<String, Double> result = new LinkedHashMap<>();
        for (int month = 1; month <= 12; month++) {
            List<ConsumptionRecord> records = consumptionDao.findByMonth(year, month);
            double total = records.stream().mapToDouble(ConsumptionRecord::getQuantity).sum();
            result.put(String.format("%02d/%04d", month, year), total);
        }
        return result;
    }

    /**
     * Returns consumption per building for a specific month.
     * Useful for multi-building comparison bar chart.
     */
    public Map<String, Double> getBuildingComparisonForMonth(int year, int month) {
        List<ConsumptionRecord> records = consumptionDao.findByMonth(year, month);
        Map<Integer, Double> byId = records.stream()
            .collect(Collectors.groupingBy(ConsumptionRecord::getBuildingId,
                Collectors.summingDouble(ConsumptionRecord::getQuantity)));

        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : byId.entrySet()) {
            Building b = buildingDao.findById(entry.getKey());
            String name = b != null ? b.getName() : "Bâtiment " + entry.getKey();
            result.put(name, entry.getValue());
        }
        return result;
    }
}

