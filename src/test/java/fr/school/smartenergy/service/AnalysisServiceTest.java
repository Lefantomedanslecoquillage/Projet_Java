package fr.school.smartenergy.service;

import fr.school.smartenergy.model.EnergyType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisServiceTest {

    @Test
    void costServiceLoadsTariffs() {
        CostService costService = new CostService();
        Map<EnergyType, Double> tariffs = costService.getTariffs();
        assertFalse(tariffs.isEmpty());
        // All energy types should have a tariff
        for (EnergyType type : EnergyType.values()) {
            assertTrue(tariffs.containsKey(type),
                "Missing tariff for: " + type);
        }
    }

    @Test
    void electricityTariffIsPositive() {
        CostService costService = new CostService();
        double tariff = costService.getTariff(EnergyType.ELECTRICITY);
        assertTrue(tariff > 0, "Electricity tariff must be positive");
    }

    @Test
    void gasTariffIsPositive() {
        CostService costService = new CostService();
        double tariff = costService.getTariff(EnergyType.GAS);
        assertTrue(tariff > 0, "Gas tariff must be positive");
    }

    @Test
    void energyTypeLabelsAreNotEmpty() {
        for (EnergyType type : EnergyType.values()) {
            assertNotNull(type.getLabel());
            assertFalse(type.getLabel().isBlank());
        }
    }

    @Test
    void energyTypeUnitsAreNotEmpty() {
        for (EnergyType type : EnergyType.values()) {
            assertNotNull(type.getUnit());
            assertFalse(type.getUnit().isBlank());
        }
    }
}

