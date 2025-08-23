package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the EthiopicChronology class.
 * Note: The original test class name and scaffolding are preserved from the source,
 * but a more descriptive name like "EthiopicChronologyTest" would be preferable.
 */
public class EthiopicChronology_ESTestTest20 extends EthiopicChronology_ESTest_scaffolding {

    // The maximum year supported by EthiopicChronology, used for boundary testing.
    // This value is copied from the private constant EthiopicChronology.MAX_YEAR.
    private static final int MAX_SUPPORTED_YEAR = 292272984;

    /**
     * Verifies that calculateFirstDayOfYearMillis computes the correct millisecond value
     * for the first day of the maximum supported year.
     *
     * This test serves as a crucial regression test for a boundary condition, ensuring
     * the calculation remains accurate and does not overflow, as the expected value is
     * close to Long.MAX_VALUE.
     */
    @Test
    public void calculateFirstDayOfYearMillis_forMaxYear_returnsCorrectValue() {
        // Arrange
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        
        // This is the pre-calculated "golden" value for the number of milliseconds
        // from the Java epoch to the beginning of the maximum supported Ethiopic year.
        long expectedFirstDayMillis = 9223371994233600000L;

        // Act
        long actualFirstDayMillis = chronology.calculateFirstDayOfYearMillis(MAX_SUPPORTED_YEAR);

        // Assert
        assertEquals(expectedFirstDayMillis, actualFirstDayMillis);
    }
}