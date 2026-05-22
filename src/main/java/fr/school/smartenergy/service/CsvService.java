package fr.school.smartenergy.service;

import fr.school.smartenergy.exception.ImportException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvService {

    public List<String[]> readCsv(String path) throws ImportException {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }

        } catch (Exception e) {
            throw new ImportException("Erreur lecture CSV", e);
        }

        return data;
    }
}