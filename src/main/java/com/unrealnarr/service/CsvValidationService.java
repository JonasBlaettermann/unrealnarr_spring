package com.unrealnarr.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.unrealnarr.entity.Critics;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CsvValidationService {
    public void validateCsvFile(CSVReader reader) throws IOException, CsvValidationException {
        String[] header = reader.readNext();
        if (header == null) {
            throw new RuntimeException("Die CSV-Datei enthält keinen Header.");
        }

        int expectedFieldCount = header.length;
        String[] line;
        int lineNumber = 0;

        while ((line = reader.readNext()) != null) {
            lineNumber++;
            if (line.length != expectedFieldCount) {
                throw new RuntimeException("Fehler in Zeile " + lineNumber + ": Anzahl der Felder stimmt nicht mit der Header-Zeile überein.");
            }
        }
    }


    public Critics parseLine(String[] line) {
        // Annahme: line enthält die CSV-Daten
        if (line.length < 14) {
            throw new RuntimeException("Fehlende Felder in der Zeile.");
        }

        Critics critic = new Critics();
        critic.setTconst(line[0]);
        critic.setTitleType(line[1]);
        critic.setGermanTitle(line[2]);
        critic.setPrimaryTitle(line[3]);
        critic.setOriginalTitle(line[4]);
        critic.setStartYear(Integer.parseInt(line[5]));
        critic.setEndYear(line[6].equals("\\N") ? 0 : Integer.parseInt(line[6])); // Setze Standardwert oder null
        critic.setRuntimeMinutes(Integer.parseInt(line[7]));
        critic.setGenres(line[8]);
        critic.setIMDBRating(Float.parseFloat(line[9]));
        critic.setRating(Float.parseFloat(line[10]));
        critic.setCritic(parseStringArray(line[11]));
        critic.setDate(parseDate(line[12]));
        critic.setTags(parseStringArray(line[13]));

        return critic;
    }

    private String[] parseStringArray(String field) {
        return field.replaceAll("[\\[\\]'\"]", "").split(",\\s*");
    }

    private Date parseDate(String dateStr) {
        if (dateStr.equals("\\N")) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Fehler beim Parsen des Datums: " + dateStr, e);
        }
    }
}