package fr.school.smartenergy.service;

import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class TestDataService {

    private final Random random = new Random();

    public ConsumptionRecord generate(int buildingId) {

        EnergyType type = EnergyType.values()[random.nextInt(EnergyType.values().length)];
        double quantity = 10 + random.nextDouble() * 100;

        return new ConsumptionRecord(
                0,
                buildingId,
                LocalDate.now(),
                LocalTime.now(),
                type,
                quantity,
                quantity * 0.2
        );
    }
}