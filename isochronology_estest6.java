package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ISOChronology} class.
 */
public class ISOChronologyTest {

    @Test
    public void toString_forUTCInstance_shouldReturnCorrectStringRepresentation() {
        // Arrange
        ISOChronology utcChronology = ISOChronology.getInstanceUTC();
        String expectedToString = "ISOChronology[UTC]";

        // Act
        String actualToString = utcChronology.toString();

        // Assert
        assertEquals(expectedToString, actualToString);
    }
}