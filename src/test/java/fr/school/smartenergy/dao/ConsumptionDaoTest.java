package fr.school.smartenergy.dao;

import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConsumptionRecord model and related logic.
 */
class ConsumptionDaoTest {

    @Test
    void consumptionRecordCreatedCorrectly() {
        LocalDate date = LocalDate.of(2026, 5, 1);
        LocalTime time = LocalTime.of(14, 30);
        ConsumptionRecord r = new ConsumptionRecord(2, date, time, EnergyType.GAS, 4.2, 0.504);

        assertEquals(2, r.getBuildingId());
        assertEquals(date, r.getDate());
        assertEquals(time, r.getTime());
        assertEquals(EnergyType.GAS, r.getEnergyType());
        assertEquals(4.2, r.getQuantity(), 0.001);
        assertEquals(0.504, r.getEstimatedCost(), 0.001);
    }

    @Test
    void consumptionRecordDefaultIdIsZero() {
        ConsumptionRecord r = new ConsumptionRecord();
        assertEquals(0, r.getId());
    }

    @Test
    void consumptionRecordSettersWork() {
        ConsumptionRecord r = new ConsumptionRecord();
        r.setId(10);
        r.setBuildingId(3);
        r.setDate(LocalDate.of(2026, 1, 15));
        r.setTime(LocalTime.of(8, 0));
        r.setEnergyType(EnergyType.ELECTRICITY);
        r.setQuantity(20.0);
        r.setEstimatedCost(5.0);

        assertEquals(10, r.getId());
        assertEquals(3, r.getBuildingId());
        assertEquals(LocalDate.of(2026, 1, 15), r.getDate());
        assertEquals(LocalTime.of(8, 0), r.getTime());
        assertEquals(EnergyType.ELECTRICITY, r.getEnergyType());
        assertEquals(20.0, r.getQuantity());
        assertEquals(5.0, r.getEstimatedCost());
    }

    @Test
    void allEnergyTypesHaveLabels() {
        for (EnergyType type : EnergyType.values()) {
            assertNotNull(type.getLabel());
            assertNotNull(type.getUnit());
        }
    }
}

