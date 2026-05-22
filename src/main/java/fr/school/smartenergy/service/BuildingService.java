package fr.school.smartenergy.service;

import fr.school.smartenergy.dao.BuildingDao;
import fr.school.smartenergy.model.Building;

import java.util.List;

public class BuildingService {

    private final BuildingDao buildingDao;

    public BuildingService() {
        this.buildingDao = new BuildingDao();
    }

    public List<Building> getAllBuildings() {
        return buildingDao.findAll();
    }

    public Building getById(int id) {
        return buildingDao.findById(id);
    }

    public Building create(Building building) {
        validate(building);
        return buildingDao.save(building);
    }

    public void update(Building building) {
        validate(building);
        buildingDao.update(building);
    }

    public void delete(int id) {
        buildingDao.delete(id);
    }

    /** Clones a building by creating a copy with a new name. */
    public Building clone(int id) {
        Building original = buildingDao.findById(id);
        if (original == null) {
            throw new IllegalArgumentException("Bâtiment introuvable : " + id);
        }
        Building clone = original.cloneWithoutId();
        return buildingDao.save(clone);
    }

    private void validate(Building building) {
        if (building == null) throw new IllegalArgumentException("Le bâtiment ne peut pas être nul.");
        if (building.getName() == null || building.getName().isBlank()) {
            throw new IllegalArgumentException("Le nom du bâtiment est obligatoire.");
        }
        if (building.getType() == null) {
            throw new IllegalArgumentException("Le type du bâtiment est obligatoire.");
        }
        if (building.getSurface() <= 0) {
            throw new IllegalArgumentException("La surface doit être positive.");
        }
        if (building.getOccupants() <= 0) {
            throw new IllegalArgumentException("Le nombre d'occupants doit être positif.");
        }
    }
}

