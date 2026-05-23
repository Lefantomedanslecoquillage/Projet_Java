package fr.school.smartenergy.dao;

import fr.school.smartenergy.exception.DatabaseException;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** DAO for CRUD operations on consumption records. */
public class ConsumptionDao {

    private final Connection connection;

    public ConsumptionDao() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public List<ConsumptionRecord> findAll() {
        String sql = "SELECT * FROM consumption_records ORDER BY date DESC, time DESC";
        return query(sql);
    }

    public List<ConsumptionRecord> findByBuilding(int buildingId) {
        String sql = "SELECT * FROM consumption_records WHERE building_id = ? ORDER BY date DESC, time DESC";
        List<ConsumptionRecord> records = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, buildingId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) records.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des relevés", e);
        }
        return records;
    }

    public List<ConsumptionRecord> findByDate(LocalDate date) {
        String sql = "SELECT * FROM consumption_records WHERE date = ?";
        List<ConsumptionRecord> records = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, date.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) records.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des relevés par date", e);
        }
        return records;
    }

    public List<ConsumptionRecord> findByMonth(int year, int month) {
        String pattern = String.format("%04d-%02d%%", year, month);
        String sql = "SELECT * FROM consumption_records WHERE date LIKE ?";
        List<ConsumptionRecord> records = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) records.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des relevés par mois", e);
        }
        return records;
    }

    public List<ConsumptionRecord> findByYear(int year) {
        String pattern = year + "-%";
        String sql = "SELECT * FROM consumption_records WHERE date LIKE ?";
        List<ConsumptionRecord> records = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) records.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des relevés par année", e);
        }
        return records;
    }

    public ConsumptionRecord save(ConsumptionRecord record) {
        String sql = "INSERT INTO consumption_records (building_id, date, time, energy_type, quantity, estimated_cost) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, record.getBuildingId());
            stmt.setString(2, record.getDate().toString());
            stmt.setString(3, record.getTime().toString());
            stmt.setString(4, record.getEnergyType().name());
            stmt.setDouble(5, record.getQuantity());
            stmt.setDouble(6, record.getEstimatedCost());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) record.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de l'enregistrement du relevé", e);
        }
        return record;
    }

    public void update(ConsumptionRecord record) {
        String sql = "UPDATE consumption_records SET building_id=?, date=?, time=?, energy_type=?, quantity=?, estimated_cost=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, record.getBuildingId());
            stmt.setString(2, record.getDate().toString());
            stmt.setString(3, record.getTime().toString());
            stmt.setString(4, record.getEnergyType().name());
            stmt.setDouble(5, record.getQuantity());
            stmt.setDouble(6, record.getEstimatedCost());
            stmt.setInt(7, record.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la mise à jour du relevé", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM consumption_records WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression du relevé", e);
        }
    }

    private List<ConsumptionRecord> query(String sql) {
        List<ConsumptionRecord> records = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) records.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des relevés", e);
        }
        return records;
    }

    private ConsumptionRecord mapRow(ResultSet rs) throws SQLException {
        ConsumptionRecord r = new ConsumptionRecord();
        r.setId(rs.getInt("id"));
        r.setBuildingId(rs.getInt("building_id"));
        r.setDate(LocalDate.parse(rs.getString("date")));
        r.setTime(LocalTime.parse(rs.getString("time")));
        r.setEnergyType(EnergyType.valueOf(rs.getString("energy_type")));
        r.setQuantity(rs.getDouble("quantity"));
        r.setEstimatedCost(rs.getDouble("estimated_cost"));
        return r;
    }
}

