package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 * This test class focuses on verifying the behavior of the StringConverter.
 */
public class StringConverterTest {

    /**
     * Tests that getPartialValues throws a NullPointerException when the provided
     * DateTimeFormatter is null. The converter should not attempt to parse the
     * string if it doesn't have a valid formatter to use.
     */
    @Test(expected = NullPointerException.class)
    public void getPartialValues_whenFormatterIsNull_throwsNullPointerException() {
        // Arrange
        StringConverter converter = StringConverter.INSTANCE;
        ReadablePartial partialTemplate = new LocalDate();
        Chronology chronology = ISOChronology.getInstanceUTC();
        String anyString = "2023-10-27"; // The actual string content is irrelevant for this test.

        // Act: Attempt to get partial values using a null formatter.
        // The @Test(expected) annotation handles the assertion, expecting a NullPointerException.
        converter.getPartialValues(partialTemplate, anyString, chronology, (DateTimeFormatter) null);
    }
}