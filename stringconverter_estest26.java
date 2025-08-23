package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.MutablePeriod;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that calling setInto() with a ReadWritablePeriod and an invalidly formatted
     * string throws an IllegalArgumentException.
     */
    @Test
    public void setIntoPeriod_withInvalidStringFormat_shouldThrowIllegalArgumentException() {
        // Arrange
        StringConverter converter = new StringConverter();
        String invalidPeriodString = "p3@QA9'OLT&K_7a]X<";
        ReadWritablePeriod period = new MutablePeriod();
        // A non-null chronology is required by the method signature.
        // ISOChronology is a standard choice.
        Chronology chronology = ISOChronology.getInstanceUTC();

        // Act & Assert
        try {
            converter.setInto(period, invalidPeriodString, chronology);
            fail("Expected an IllegalArgumentException to be thrown for the invalid period format.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the invalid format.
            String expectedMessage = "Invalid format: \"" + invalidPeriodString + "\"";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}