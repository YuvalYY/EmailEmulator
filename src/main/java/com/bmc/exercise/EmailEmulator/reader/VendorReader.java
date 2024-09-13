package com.bmc.exercise.EmailEmulator.reader;

import com.bmc.exercise.EmailEmulator.model.Vendor;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for reading information about the vendors on startup
 */
@Component
public class VendorReader {
    /**
     * Path to the resource file of the vendors
     */
    private final String csvFilePath;

    public VendorReader(@Value("${bmc.exercise.server.csv.path}") String inCSVFilePath) {
        csvFilePath = inCSVFilePath;
    }

    /**
     * Reads information about the vendors
     *
     * @return Map from each vendor's email postfix to the vendor
     * @throws IOException In case the resource file can't be read
     * @throws CsvException If the CSV file is invalid
     */
    public Map<String, Vendor> readToMap() throws IOException, CsvException {
        Map<String, Vendor> outVendorMap = new HashMap<>();

        ResourceLoader lResourceLoader = new DefaultResourceLoader();
        Resource lVendorResource = lResourceLoader.getResource("classpath:" + csvFilePath);
        InputStreamReader lFileReader = new InputStreamReader(lVendorResource.getInputStream());

        try (CSVReader lCSVReader = new CSVReaderBuilder(lFileReader).build()) {
            List<String[]> lRowList = lCSVReader.readAll();
            for (String[] lRow : lRowList) {
                Vendor lVendor = new Vendor(lRow[0], lRow[1], lRow[2], lRow[3]);
                outVendorMap.put(lVendor.getEmailPostfix(), lVendor);
            }
        }

        return outVendorMap;
    }
}
