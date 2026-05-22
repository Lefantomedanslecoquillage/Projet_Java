package fr.school.smartenergy.dao;

import fr.school.smartenergy.exception.DatabaseException;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConsumptionDao {

    public void insert(ConsumptionRecord c) throws DatabaseException {
        String sql = "INSERT INTO consumption(building_id, date, time, energy_type, quantity, cost) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, c.getBuildingId());
            stmt.setString(2, c.getDate().toString());
            stmt.setString(3, c.getTime().toString());
            stmt.setString(4, c.getEnergyType().name());
            stmt.setDouble(5, c.getQuantity());
            stmt.setDouble(6, c.getEstimatedCost());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erreur insertion consommation", e);
        }
    }

    public List<ConsumptionRecord> findByBuilding(int buildingId) throws DatabaseException {
        List<ConsumptionRecord> list = new ArrayList<>();

        String sql = "SELECT * FROM consumption WHERE building_id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, buildingId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ConsumptionRecord c = new ConsumptionRecord();

                c.setId(rs.getInt("id"));
                c.setBuildingId(rs.getInt("building_id"));
                c.setDate(LocalDate.parse(rs.getString("date")));
                c.setTime(LocalTime.parse(rs.getString("time")));
                c.setEnergyType(EnergyType.valueOf(rs.getString("energy_type")));
                c.setQuantity(rs.getDouble("quantity"));
                c.setEstimatedCost(rs.getDouble("cost"));

                list.add(c);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erreur lecture consommations", e);
        }

        return list;
    }

    public List<ConsumptionRecord> findAll() throws DatabaseException {
        List<ConsumptionRecord> list = new ArrayList<>();

        String sql = "SELECT * FROM consumption";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ConsumptionRecord c = new ConsumptionRecord();

                c.setId(rs.getInt("id"));
                c.setBuildingId(rs.getInt("building_id"));
                c.setDate(LocalDate.parse(rs.getString("date")));
                c.setTime(LocalTime.parse(rs.getString("time")));
                c.setEnergyType(EnergyType.valueOf(rs.getString("energy_type")));
                c.setQuantity(rs.getDouble("quantity"));
                c.setEstimatedCost(rs.getDouble("cost"));

                list.add(c);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erreur lecture consommations", e);
        }

        return list;
    }

    public void deleteByBuilding(int buildingId) throws DatabaseException {
        String sql = "DELETE FROM consumption WHERE building_id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, buildingId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erreur suppression consommations", e);
        }
    }
}