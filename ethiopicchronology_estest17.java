package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * An improved, more understandable unit test for the EthiopicChronology class.
 * This class focuses on verifying the behavior of the getMinYear() method.
 */
public class EthiopicChronologyImprovedTest {

    /**
     * Tests that getMinYear() returns the correct, predefined minimum supported year.
     * <p>
     * The EthiopicChronology class defines a constant for the minimum year, and this test
     * verifies that the getter method consistently returns this exact value, regardless
     * of the chronology's time zone.
     */
    @Test
    public void getMinYear_shouldReturnPredefinedConstantMinimumYear() {
        // Arrange
        // The minimum year is a constant and does not depend on the time zone.
        // We use the UTC instance for simplicity and clarity, as any instance would yield the same result.
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();
        
        // This is the hardcoded minimum year value defined as a private constant
        // within the EthiopicChronology class.
        final int expectedMinYear = -292269337;

        // Act
        // Retrieve the minimum year from the chronology instance.
        int actualMinYear = ethiopicChronology.getMinYear();

        // Assert
        // Verify that the returned value matches the expected constant.
        assertEquals("The minimum year should match the predefined constant value.",
                expectedMinYear, actualMinYear);
    }
}