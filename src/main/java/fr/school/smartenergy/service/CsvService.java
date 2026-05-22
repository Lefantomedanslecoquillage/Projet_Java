package fr.school.smartenergy.service;

import fr.school.smartenergy.exception.ImportException;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;
import fr.school.smartenergy.util.CsvUtils;
import fr.school.smartenergy.util.DateUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses CSV files and converts rows into ConsumptionRecord objects.
 * Expected CSV format: building_id,date,time,energy_type,quantity
 */
public class CsvService {

    private static final int EXPECTED_COLUMNS = 5;
    private static final String[] EXPECTED_HEADERS = {"building_id", "date", "time", "energy_type", "quantity"};

    private final CostService costService;

    public CsvService(CostService costService) {
        this.costService = costService;
    }

    public List<ConsumptionRecord> importFromFile(Path filePath) {
        List<String[]> rows;
        try {
            rows = CsvUtils.readFile(filePath);
        } catch (IOException e) {
            throw new ImportException("Impossible de lire le fichier CSV : " + filePath, e);
        }
        return parseRows(rows);
    }

    private List<ConsumptionRecord> parseRows(List<String[]> rows) {
        if (rows.isEmpty()) {
            throw new ImportException("Le fichier CSV est vide.");
        }

        // Validate header
        String[] header = rows.get(0);
        validateHeader(header);

        List<ConsumptionRecord> records = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            try {
                records.add(parseRow(row, i + 1));
            } catch (Exception e) {
                throw new ImportException("Erreur à la ligne " + (i + 1) + " : " + e.getMessage(), e);
            }
        }
        return records;
    }

    private void validateHeader(String[] header) {
        if (header.length < EXPECTED_COLUMNS) {
            throw new ImportException("En-tête CSV invalide. Colonnes attendues : building_id,date,time,energy_type,quantity");
        }
        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            if (!EXPECTED_HEADERS[i].equalsIgnoreCase(header[i])) {
                throw new ImportException("Colonne " + (i + 1) + " attendue : " + EXPECTED_HEADERS[i] + ", trouvée : " + header[i]);
            }
        }
    }

    private ConsumptionRecord parseRow(String[] row, int lineNumber) {
        if (row.length < EXPECTED_COLUMNS) {
            throw new ImportException("Ligne " + lineNumber + " incomplète.");
        }

        int buildingId = Integer.parseInt(row[0]);
        LocalDate date = DateUtils.parseDate(row[1]);
        LocalTime time = DateUtils.parseTime(row[2]);
        EnergyType energyType = EnergyType.valueOf(row[3].toUpperCase());
        double quantity = Double.parseDouble(row[4]);

        if (quantity < 0) {
            throw new ImportException("La quantité ne peut pas être négative (ligne " + lineNumber + ").");
        }

        double cost = costService.estimateCost(energyType, quantity);
        return new ConsumptionRecord(buildingId, date, time, energyType, quantity, cost);
    }
}

