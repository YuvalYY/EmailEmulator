package com.bmc.exercise.EmailEmulator.configuration;

import com.bmc.exercise.EmailEmulator.model.Vendor;
import com.bmc.exercise.EmailEmulator.reader.VendorReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class EmailEmulatorConfiguration {
    private static final Logger LOGGER = LogManager.getLogger(EmailEmulatorConfiguration.class);

    /**
     * Provides a map from each vendor's email postfix to the vendor
     *
     * @param inVendorReader Reader which provides information about the vendors
     * @return A bean with information about the vendors
     * @throws IOException In case the vendors file couldn't be read
     * @throws CsvException In case the vendors file isn't a valid CSV file
     */
    @Bean
    @Autowired
    public Map<String, Vendor> vendors(VendorReader inVendorReader) throws IOException, CsvException {
        try {
            return inVendorReader.readToMap();
        }
        catch (IOException inEx) {
            LOGGER.fatal("Vendor file can't be read");
            throw inEx;
        }
        catch (CsvException inEx) {
            LOGGER.fatal("Vendor file isn't a valid CSV file");
            throw inEx;
        }
    }

    /**
     * Provides the executor of SMTP tasks
     *
     * @param inThreadNum The number of threads for the executor
     * @return The executor bean
     */
    @Bean(destroyMethod = "shutdown")
    public ExecutorService smtpTaskExecutor(@Value("${bmc.exercise.server.smtp_task.threads}") int inThreadNum) {
        return Executors.newFixedThreadPool(inThreadNum);
    }
}
