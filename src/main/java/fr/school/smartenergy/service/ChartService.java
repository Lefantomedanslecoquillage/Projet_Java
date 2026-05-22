package fr.school.smartenergy.service;

import fr.school.smartenergy.model.ConsumptionRecord;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartService {

    public Map<String, Double> consumptionByEnergy(List<ConsumptionRecord> list) {
        return list.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getEnergyType().name(),
                        Collectors.summingDouble(ConsumptionRecord::getQuantity)
                ));
    }
}