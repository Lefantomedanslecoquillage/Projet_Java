package fr.school.smartenergy.service;

import fr.school.smartenergy.dao.BuildingDao;
import fr.school.smartenergy.dao.ConsumptionDao;
import fr.school.smartenergy.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

/**
 * Generates sample buildings and consumption records for testing and demonstration.
 */
public class TestDataService {

    private final BuildingDao buildingDao;
    private final ConsumptionDao consumptionDao;
    private final CostService costService;
    private final Random random;

    public TestDataService() {
        this.buildingDao = new BuildingDao();
        this.consumptionDao = new ConsumptionDao();
        this.costService = new CostService();
        this.random = new Random(42);
    }

    /** Generates 3 sample buildings and 90 days of consumption records per building. */
    public void generateSampleData() {
        List<Building> existing = buildingDao.findAll();
        if (!existing.isEmpty()) return; // Avoid duplicating data

        // Create sample buildings
        Building b1 = buildingDao.save(new Building("Maison Dupont", BuildingType.MAISON, "12 rue des Lilas, Paris", 120.0, 4));
        Building b2 = buildingDao.save(new Building("Appartement Centre", BuildingType.APPARTEMENT, "5 av. de la République, Lyon", 65.0, 2));
        Building b3 = buildingDao.save(new Building("Bureau Principal", BuildingType.BUREAU, "8 rue du Commerce, Marseille", 250.0, 20));

        // Generate 90 days of records for each building
        for (Building building : List.of(b1, b2, b3)) {
            generateRecords(building.getId(), 90);
        }
    }

    private void generateRecords(int buildingId, int days) {
        LocalDate today = LocalDate.now();
        EnergyType[] types = EnergyType.values();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            // 1 to 3 records per day
            int recordsPerDay = 1 + random.nextInt(3);
            for (int j = 0; j < recordsPerDay; j++) {
                EnergyType type = types[random.nextInt(types.length)];
                double quantity = 1.0 + random.nextDouble() * 30.0;
                quantity = Math.round(quantity * 100.0) / 100.0;
                double cost = costService.estimateCost(type, quantity);
                LocalTime time = LocalTime.of(random.nextInt(24), random.nextInt(60));
                consumptionDao.save(new ConsumptionRecord(buildingId, date, time, type, quantity, cost));
            }
        }
    }

    /** Clears all data (for reset in tests or demos). */
    public void clearAllData() {
        List<Building> buildings = buildingDao.findAll();
        for (Building b : buildings) {
            buildingDao.delete(b.getId());
        }
    }
}

