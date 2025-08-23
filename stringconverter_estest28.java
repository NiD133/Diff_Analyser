package org.joda.time.convert;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link StringConverter} class, focusing on period conversion.
 */
public class StringConverterTest {

    /**
     * Tests that setInto() correctly parses a standard ISO-8601 period string
     * and updates the provided ReadWritablePeriod object.
     */
    @Test
    public void setInto_shouldParseIsoPeriodStringAndUpdatePeriod() {
        // Arrange
        StringConverter converter = StringConverter.INSTANCE;
        String periodString = "PT2S"; // An ISO-8601 period string for 2 seconds.

        // Initialize with non-zero values to ensure the method clears the period before setting new values.
        MutablePeriod actualPeriod = new MutablePeriod(1, 1, 1, 1, 1, 1, 1, 1);
        Period expectedPeriod = new Period().withSeconds(2);

        // Act
        // The chronology parameter can be null, as it defaults to the standard ISO chronology.
        converter.setInto(actualPeriod, periodString, null);

        // Assert
        assertEquals("The period should be cleared and updated to 2 seconds.", expectedPeriod, actualPeriod);
    }
}