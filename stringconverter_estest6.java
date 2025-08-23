package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.MutablePeriod;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.chrono.IslamicChronology;
import org.junit.Test;

/**
 * This test class contains an improved version of a test for the {@link StringConverter}.
 * The original test was auto-generated and lacked clarity.
 */
public class StringConverter_ESTestTest6 { // Scaffolding dependency removed for a standalone example

    /**
     * Tests that setInto(ReadWritablePeriod, ...) throws a ClassCastException
     * when the object to be converted is not a String.
     * <p>
     * The StringConverter is designed to convert *from* a String, so passing any
     * other object type is a contract violation and should fail with a ClassCastException.
     */
    @Test(expected = ClassCastException.class)
    public void setIntoPeriod_shouldThrowException_whenConvertingNonStringObject() {
        // Arrange
        StringConverter converter = new StringConverter();
        ReadWritablePeriod period = new MutablePeriod();
        Chronology chronology = IslamicChronology.getInstance();

        // The object to be converted is intentionally a MutablePeriod, not a String,
        // to test the type-checking behavior of the converter.
        Object nonStringObject = new MutablePeriod();

        // Act & Assert
        // This call is expected to throw a ClassCastException because the converter
        // internally casts the object to a String before processing.
        converter.setInto(period, nonStringObject, chronology);
    }
}