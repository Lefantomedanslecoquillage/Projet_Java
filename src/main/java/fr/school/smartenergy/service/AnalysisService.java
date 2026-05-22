package fr.school.smartenergy.service;

import fr.school.smartenergy.dao.BuildingDao;
import fr.school.smartenergy.dao.ConsumptionDao;
import fr.school.smartenergy.model.Alert;
import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalysisService {

    private final ConsumptionDao consumptionDao;
    private final BuildingDao buildingDao;

    public AnalysisService() {
        this.consumptionDao = new ConsumptionDao();
        this.buildingDao = new BuildingDao();
    }

    /** Returns the building with the highest total consumption this year. */
    public Building findTopBuilding() {
        List<ConsumptionRecord> yearly = consumptionDao.findByYear(LocalDate.now().getYear());
        if (yearly.isEmpty()) return null;

        Map<Integer, Double> byBuilding = yearly.stream()
            .collect(Collectors.groupingBy(ConsumptionRecord::getBuildingId,
                Collectors.summingDouble(ConsumptionRecord::getQuantity)));

        int topId = byBuilding.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(-1);

        return topId == -1 ? null : buildingDao.findById(topId);
    }

    /** Returns the most used energy type this year. */
    public EnergyType findDominantEnergyType() {
        List<ConsumptionRecord> yearly = consumptionDao.findByYear(LocalDate.now().getYear());
        if (yearly.isEmpty()) return null;

        Map<EnergyType, Double> byType = yearly.stream()
            .collect(Collectors.groupingBy(ConsumptionRecord::getEnergyType,
                Collectors.summingDouble(ConsumptionRecord::getQuantity)));

        return byType.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    /** Estimates the monthly bill by averaging the last 3 months. */
    public double estimateMonthlyBill() {
        LocalDate now = LocalDate.now();
        double total = 0;
        int months = 0;
        for (int i = 0; i < 3; i++) {
            LocalDate m = now.minusMonths(i);
            List<ConsumptionRecord> records = consumptionDao.findByMonth(m.getYear(), m.getMonthValue());
            if (!records.isEmpty()) {
                total += records.stream().mapToDouble(ConsumptionRecord::getEstimatedCost).sum();
                months++;
            }
        }
        return months == 0 ? 0 : total / months;
    }

    /**
     * Detects consumption peaks: records whose quantity is more than 2x
     * the average quantity for the same energy type this year.
     */
    public List<ConsumptionRecord> detectPeaks() {
        List<ConsumptionRecord> yearly = consumptionDao.findByYear(LocalDate.now().getYear());
        if (yearly.isEmpty()) return List.of();

        Map<EnergyType, Double> avgByType = yearly.stream()
            .collect(Collectors.groupingBy(ConsumptionRecord::getEnergyType,
                Collectors.averagingDouble(ConsumptionRecord::getQuantity)));

        return yearly.stream()
            .filter(r -> r.getQuantity() > 2 * avgByType.getOrDefault(r.getEnergyType(), 0.0))
            .sorted(Comparator.comparingDouble(ConsumptionRecord::getQuantity).reversed())
            .collect(Collectors.toList());
    }

    /** Generates simple textual alerts based on analysis. */
    public List<Alert> generateAlerts() {
        List<Alert> alerts = new ArrayList<>();

        Building top = findTopBuilding();
        if (top != null) {
            alerts.add(new Alert("Bâtiment le plus consommateur : " + top.getName(), Alert.Level.INFO));
        }

        EnergyType dominant = findDominantEnergyType();
        if (dominant != null) {
            alerts.add(new Alert("Énergie dominante : " + dominant.getLabel(), Alert.Level.INFO));
        }

        List<ConsumptionRecord> peaks = detectPeaks();
        if (!peaks.isEmpty()) {
            alerts.add(new Alert("Pics de consommation détectés : " + peaks.size() + " relevé(s) anormal/anormaux", Alert.Level.WARNING));
        }

        double bill = estimateMonthlyBill();
        if (bill > 0) {
            alerts.add(new Alert(String.format("Facture mensuelle estimée : %.2f €", bill), Alert.Level.INFO));
        }

        return alerts;
    }
}

