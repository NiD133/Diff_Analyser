package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.MutablePeriod;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Test;

/**
 * Unit tests for {@link StringConverter}.
 */
// The original test class name and inheritance are preserved.
public class StringConverter_ESTestTest27 extends StringConverter_ESTest_scaffolding {

    /**
     * Tests that calling setInto() with an empty string for a period
     * results in an IllegalArgumentException, as it's an invalid format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setIntoPeriod_withEmptyString_shouldThrowIllegalArgumentException() {
        // Arrange
        StringConverter converter = new StringConverter();
        ReadWritablePeriod period = new MutablePeriod();
        Chronology chronology = GregorianChronology.getInstanceUTC();
        String invalidPeriodString = "";

        // Act
        converter.setInto(period, invalidPeriodString, chronology);

        // Assert: Exception is expected and handled by the @Test annotation.
    }
}