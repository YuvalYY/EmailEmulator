package com.bmc.exercise.EmailEmulator.reader;

import com.bmc.exercise.EmailEmulator.model.Vendor;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

/**
 * Test class for {@link VendorReader}
 */
@SpringBootTest
public class VendorReaderTest {
    @Autowired
    private VendorReader vendorReader;

    /**
     * Test case for parsing vendors from a resource CSV file into a map
     *
     * @throws IOException In case the vendors file couldn't be read
     * @throws CsvException In case the vendors file isn't a valid CSV file
     */
    @Test
    public void testReadToMap() throws IOException, CsvException {
        Map<String, Vendor> lVendorMap = vendorReader.readToMap();

        Assertions.assertEquals(1, lVendorMap.size());
        Vendor lVendor = lVendorMap.get("testmail.com");
        Assertions.assertNotNull(lVendor);
        Assertions.assertEquals("smtp.testmail.com", lVendor.getServerAddress());
        Assertions.assertEquals("admin", lVendor.getUsername());
        Assertions.assertEquals("admin", lVendor.getPassword());
        Assertions.assertEquals("testmail.com", lVendor.getEmailPostfix());
    }
}
