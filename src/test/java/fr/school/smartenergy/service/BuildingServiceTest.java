package fr.school.smartenergy.service;

import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.BuildingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingServiceTest {

    private Building building;

    @BeforeEach
    void setup() {
        building = new Building("Maison Test", BuildingType.MAISON, "1 rue Test", 100.0, 3);
    }

    @Test
    void buildingFieldsAreCorrectlySet() {
        assertEquals("Maison Test", building.getName());
        assertEquals(BuildingType.MAISON, building.getType());
        assertEquals("1 rue Test", building.getAddress());
        assertEquals(100.0, building.getSurface());
        assertEquals(3, building.getOccupants());
    }

    @Test
    void cloneWithoutIdProducesCorrectCopy() {
        Building clone = building.cloneWithoutId();
        assertEquals(0, clone.getId()); // ID not set
        assertEquals("Copie de " + building.getName(), clone.getName());
        assertEquals(building.getType(), clone.getType());
        assertEquals(building.getSurface(), clone.getSurface());
        assertEquals(building.getOccupants(), clone.getOccupants());
    }

    @Test
    void buildingToStringContainsNameAndType() {
        String str = building.toString();
        assertTrue(str.contains(building.getName()));
        assertTrue(str.contains(building.getType().getLabel()));
    }
}

