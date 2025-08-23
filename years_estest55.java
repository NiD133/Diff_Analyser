package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that the toString() method returns the period in the standard ISO8601 format "PnY".
     */
    @Test
    public void toString_shouldReturnISO8601Format() {
        // Arrange
        Years twoYears = Years.TWO;
        String expectedFormat = "P2Y";

        // Act
        String actualFormat = twoYears.toString();

        // Assert
        assertEquals(expectedFormat, actualFormat);
    }
}