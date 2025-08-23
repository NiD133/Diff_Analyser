package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the EthiopicChronology class.
 */
public class EthiopicChronologyTest {

    // The expected maximum year value, based on the private constant MAX_YEAR in EthiopicChronology.
    private static final int EXPECTED_MAX_YEAR = 292272984;

    @Test
    public void getMaxYear_shouldReturnConstantMaximumSupportedYear() {
        // Arrange: Get an instance of the chronology. The time zone does not affect this value.
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();

        // Act: Call the method under test.
        int actualMaxYear = ethiopicChronology.getMaxYear();

        // Assert: Verify that the method returns the expected constant value for the maximum year.
        assertEquals("The maximum supported year should be the expected constant value.",
                     EXPECTED_MAX_YEAR, actualMaxYear);
    }
}