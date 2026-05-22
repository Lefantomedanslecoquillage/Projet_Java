package fr.school.smartenergy.dao;

import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.BuildingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Building model and DAO logic.
 * Note: DAO tests that require a database connection
 * depend on DatabaseManager being initialized.
 */
class BuildingDaoTest {

    @Test
    void buildingCanBeCreatedWithAllFields() {
        Building b = new Building("Test", BuildingType.BUREAU, "10 rue A", 200.0, 15);
        assertEquals("Test", b.getName());
        assertEquals(BuildingType.BUREAU, b.getType());
        assertEquals("10 rue A", b.getAddress());
        assertEquals(200.0, b.getSurface());
        assertEquals(15, b.getOccupants());
    }

    @Test
    void buildingIdDefaultsToZero() {
        Building b = new Building();
        assertEquals(0, b.getId());
    }

    @Test
    void buildingSettersAndGettersWork() {
        Building b = new Building();
        b.setId(42);
        b.setName("Mon Bureau");
        b.setType(BuildingType.LOCAL_COMMERCIAL);
        b.setAddress("5 av. Test");
        b.setSurface(75.5);
        b.setOccupants(5);

        assertEquals(42, b.getId());
        assertEquals("Mon Bureau", b.getName());
        assertEquals(BuildingType.LOCAL_COMMERCIAL, b.getType());
        assertEquals("5 av. Test", b.getAddress());
        assertEquals(75.5, b.getSurface());
        assertEquals(5, b.getOccupants());
    }

    @Test
    void cloneWithoutIdProducesNewName() {
        Building b = new Building("Original", BuildingType.MAISON, "Addr", 80.0, 2);
        Building clone = b.cloneWithoutId();
        assertEquals("Copie de Original", clone.getName());
        assertEquals(0, clone.getId());
    }

    @Test
    void allBuildingTypesHaveLabels() {
        for (BuildingType type : BuildingType.values()) {
            assertNotNull(type.getLabel());
            assertFalse(type.getLabel().isBlank());
        }
    }
}

