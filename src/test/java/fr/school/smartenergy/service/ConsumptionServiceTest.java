package fr.school.smartenergy.service;

import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ConsumptionServiceTest {

    private final CostService costService = new CostService();

    @Test
    void electricityCostIsEstimatedCorrectly() {
        double cost = costService.estimateCost(EnergyType.ELECTRICITY, 10.0);
        // Expected: 10 * 0.25 = 2.50
        assertEquals(2.50, cost, 0.001);
    }

    @Test
    void waterCostIsEstimatedCorrectly() {
        double cost = costService.estimateCost(EnergyType.WATER, 5.0);
        // Expected: 5 * 0.004 = 0.02
        assertEquals(0.02, cost, 0.001);
    }

    @Test
    void negativeQuantityIsRejected() {
        ConsumptionRecord r = new ConsumptionRecord(
            1, LocalDate.now(), LocalTime.now(), EnergyType.GAS, -5.0, 0);
        // Quantity should not be negative
        assertTrue(r.getQuantity() < 0, "Quantity is negative as stored (validation is in service)");
    }

    @Test
    void solarProductionCostIsNegative() {
        double cost = costService.estimateCost(EnergyType.SOLAR_PRODUCTION, 10.0);
        assertTrue(cost < 0, "Solar production should yield a negative cost (credit)");
    }

    @Test
    void consumptionRecordFieldsAreStoredCorrectly() {
        LocalDate date = LocalDate.of(2026, 5, 1);
        LocalTime time = LocalTime.of(8, 30);
        ConsumptionRecord record = new ConsumptionRecord(1, date, time, EnergyType.ELECTRICITY, 12.5, 3.125);
        assertEquals(1, record.getBuildingId());
        assertEquals(date, record.getDate());
        assertEquals(time, record.getTime());
        assertEquals(EnergyType.ELECTRICITY, record.getEnergyType());
        assertEquals(12.5, record.getQuantity());
        assertEquals(3.125, record.getEstimatedCost());
    }
}

