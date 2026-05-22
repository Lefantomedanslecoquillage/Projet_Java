package fr.school.smartenergy.service;

import fr.school.smartenergy.dao.ConsumptionDao;
import fr.school.smartenergy.exception.DatabaseException;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ConsumptionService {

    private final ConsumptionDao dao = new ConsumptionDao();

    public void addConsumption(ConsumptionRecord c) throws DatabaseException {
        validate(c);
        dao.insert(c);
    }

    public List<ConsumptionRecord> getAll() throws DatabaseException {
        return dao.findAll();
    }

    public List<ConsumptionRecord> getByBuilding(int buildingId) throws DatabaseException {
        return dao.findByBuilding(buildingId);
    }

    public double totalConsumption(int buildingId) throws DatabaseException {
        return getByBuilding(buildingId)
                .stream()
                .mapToDouble(ConsumptionRecord::getQuantity)
                .sum();
    }

    public double totalCost(int buildingId) throws DatabaseException {
        return getByBuilding(buildingId)
                .stream()
                .mapToDouble(ConsumptionRecord::getEstimatedCost)
                .sum();
    }

    public List<ConsumptionRecord> filterByEnergy(EnergyType type) throws DatabaseException {
        return getAll().stream()
                .filter(c -> c.getEnergyType() == type)
                .collect(Collectors.toList());
    }

    public List<ConsumptionRecord> filterByDate(LocalDate date) throws DatabaseException {
        return getAll().stream()
                .filter(c -> c.getDate().equals(date))
                .collect(Collectors.toList());
    }

    private void validate(ConsumptionRecord c) {
        if (c.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantité invalide");
        }
    }
}