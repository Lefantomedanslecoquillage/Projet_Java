package fr.school.smartenergy.service;

import fr.school.smartenergy.exception.DatabaseException;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AnalysisService {

    private final ConsumptionService service = new ConsumptionService();

    public Optional<ConsumptionRecord> findPeak() throws DatabaseException {
        return service.getAll().stream()
                .max(Comparator.comparingDouble(ConsumptionRecord::getQuantity));
    }

    public EnergyType mostUsedEnergy() throws DatabaseException {
        return service.getAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        ConsumptionRecord::getEnergyType,
                        java.util.stream.Collectors.summingDouble(ConsumptionRecord::getQuantity)
                ))
                .entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse(null);
    }
}