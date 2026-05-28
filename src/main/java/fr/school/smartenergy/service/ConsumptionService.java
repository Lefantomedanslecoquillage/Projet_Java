package fr.school.smartenergy.service;

import fr.school.smartenergy.dao.ConsumptionDao;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ConsumptionService {

    private final ConsumptionDao consumptionDao;
    private final CostService costService;
    private final CsvService csvService;

    public ConsumptionService() {
        this.consumptionDao = new ConsumptionDao();
        this.costService = new CostService();
        this.csvService = new CsvService(costService);
    }

    public List<ConsumptionRecord> getAllRecords() {
        return consumptionDao.findAll();
    }

    public List<ConsumptionRecord> getByBuilding(int buildingId) {
        return consumptionDao.findByBuilding(buildingId);
    }

    public List<ConsumptionRecord> getByDate(LocalDate date) {
        return consumptionDao.findByDate(date);
    }

    public List<ConsumptionRecord> getByMonth(int year, int month) {
        return consumptionDao.findByMonth(year, month);
    }

    public List<ConsumptionRecord> getByYear(int year) {
        return consumptionDao.findByYear(year);
    }

    public ConsumptionRecord add(int buildingId, LocalDate date, LocalTime time,
                                  EnergyType energyType, double quantity) {
        validate(quantity);
        double cost = costService.estimateCost(energyType, quantity);
        ConsumptionRecord record = new ConsumptionRecord(buildingId, date, time, energyType, quantity, cost);
        return consumptionDao.save(record);
    }

    public void update(ConsumptionRecord record) {
        validate(record.getQuantity());
        // Recalculate cost in case quantity or type changed
        double cost = costService.estimateCost(record.getEnergyType(), record.getQuantity());
        record.setEstimatedCost(cost);
        consumptionDao.update(record);
    }

    public void delete(int id) {
        consumptionDao.delete(id);
    }

    /** Imports records from a CSV file and saves them all. */
    public int importFromCsv(Path filePath) {
        List<ConsumptionRecord> records = csvService.importFromFile(filePath);
        for (ConsumptionRecord record : records) {
            consumptionDao.save(record);
        }
        return records.size();
    }

    /** Estimates cost without saving. */
    public double estimateCost(EnergyType energyType, double quantity) {
        return costService.estimateCost(energyType, quantity);
    }

    private void validate(double quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative.");
        }
    }
}

