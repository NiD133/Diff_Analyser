package org.joda.time.convert;

import org.joda.time.chrono.CopticChronology;
import org.junit.Test;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that getInstantMillis throws an IllegalArgumentException when attempting to parse
     * a string representing year zero, as it is not supported by the Coptic chronology.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getInstantMillis_whenParsingUnsupportedYearZero_shouldThrowException() {
        // Arrange
        StringConverter converter = new StringConverter();
        CopticChronology copticChronology = CopticChronology.getInstance();
        String invalidDateString = "000";

        // Act
        // This call is expected to throw an IllegalArgumentException because year 0 is invalid.
        converter.getInstantMillis(invalidDateString, copticChronology);

        // Assert is handled by the 'expected' attribute of the @Test annotation.
    }
}