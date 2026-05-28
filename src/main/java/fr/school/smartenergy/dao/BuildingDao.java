package fr.school.smartenergy.dao;

import fr.school.smartenergy.exception.DatabaseException;
import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.BuildingType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO for CRUD operations on buildings. */
public class BuildingDao {

    private final Connection connection;

    public BuildingDao() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public List<Building> findAll() {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT id, name, type, address, surface, occupants FROM buildings ORDER BY name";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                buildings.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des bâtiments", e);
        }
        return buildings;
    }

    public Building findById(int id) {
        String sql = "SELECT id, name, type, address, surface, occupants FROM buildings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération du bâtiment id=" + id, e);
        }
        return null;
    }

    public Building save(Building building) {
        String sql = "INSERT INTO buildings (name, type, address, surface, occupants) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, building.getName());
            stmt.setString(2, building.getType().name());
            stmt.setString(3, building.getAddress());
            stmt.setDouble(4, building.getSurface());
            stmt.setInt(5, building.getOccupants());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) building.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de l'enregistrement du bâtiment", e);
        }
        return building;
    }

    public void update(Building building) {
        String sql = "UPDATE buildings SET name=?, type=?, address=?, surface=?, occupants=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, building.getName());
            stmt.setString(2, building.getType().name());
            stmt.setString(3, building.getAddress());
            stmt.setDouble(4, building.getSurface());
            stmt.setInt(5, building.getOccupants());
            stmt.setInt(6, building.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la mise à jour du bâtiment", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM buildings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression du bâtiment", e);
        }
    }

    private Building mapRow(ResultSet rs) throws SQLException {
        Building b = new Building();
        b.setId(rs.getInt("id"));
        b.setName(rs.getString("name"));
        b.setType(BuildingType.valueOf(rs.getString("type")));
        b.setAddress(rs.getString("address"));
        b.setSurface(rs.getDouble("surface"));
        b.setOccupants(rs.getInt("occupants"));
        return b;
    }
}

