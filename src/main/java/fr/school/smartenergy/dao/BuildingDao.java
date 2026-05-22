package fr.school.smartenergy.dao;


import fr.school.smartenergy.exception.DatabaseException;
import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.BuildingType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuildingDao {

    public void insert(Building b) throws DatabaseException {
        String sql = "INSERT INTO buildings(name, address, type, surface) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, b.getName());
            stmt.setString(2, b.getAddress());
            stmt.setString(3, b.getType().name());
            stmt.setDouble(4, b.getSurface());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erreur insertion bâtiment", e);
        }
    }

    public List<Building> findAll() throws DatabaseException {
        List<Building> list = new ArrayList<>();

        String sql = "SELECT * FROM buildings";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Building b = new Building();

                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setAddress(rs.getString("address"));
                b.setType(BuildingType.valueOf(rs.getString("type")));
                b.setSurface(rs.getDouble("surface"));

                list.add(b);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erreur lecture bâtiments", e);
        }

        return list;
    }

    public void update(Building b) throws DatabaseException {
        String sql = "UPDATE buildings SET name=?, address=?, type=?, surface=? WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, b.getName());
            stmt.setString(2, b.getAddress());
            stmt.setString(3, b.getType().name());
            stmt.setDouble(4, b.getSurface());
            stmt.setInt(5, b.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erreur update bâtiment", e);
        }
    }

    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM buildings WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erreur suppression bâtiment", e);
        }
    }
}