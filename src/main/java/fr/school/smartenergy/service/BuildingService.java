package fr.school.smartenergy.service;

import fr.school.smartenergy.dao.BuildingDao;
import fr.school.smartenergy.exception.DatabaseException;
import fr.school.smartenergy.model.Building;

import java.util.List;

public class BuildingService {

    private final BuildingDao buildingDao = new BuildingDao();

    public void addBuilding(Building b) throws DatabaseException {
        validateBuilding(b);
        buildingDao.insert(b);
    }

    public List<Building> getAllBuildings() throws DatabaseException {
        return buildingDao.findAll();
    }

    public void updateBuilding(Building b) throws DatabaseException {
        validateBuilding(b);
        buildingDao.update(b);
    }

    public void deleteBuilding(int id) throws DatabaseException {
        buildingDao.delete(id);
    }

    public Building cloneBuilding(Building b) {
        return b.cloneBuilding();
    }

    private void validateBuilding(Building b) {
        if (b.getName() == null || b.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (b.getSurface() < 0) {
            throw new IllegalArgumentException("Surface invalide");
        }
    }
}
