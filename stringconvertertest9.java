package org.joda.time.convert;

import java.util.Arrays;
import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;

/**
 * Tests the {@link StringConverter#getPartialValues(ReadablePartial, Object, Chronology)} method.
 */
public class StringConverterGetPartialValuesTest extends TestCase {

    /**
     * Tests that a time string is correctly parsed into its constituent partial values
     * based on a provided template.
     */
    public void testGetPartialValues_shouldParseTimeOfDayFromString() {
        // Arrange
        // The StringConverter uses the ReadablePartial argument as a template to determine
        // which fields to parse. A TimeOfDay instance specifies that we are interested
        // in hour, minute, second, and millisecond fields.
        final ReadablePartial partialTemplate = new TimeOfDay();
        final String timeString = "T03:04:05.006";
        final Chronology chronology = ISOChronology.getInstance();

        // The expected values correspond to [hour, minute, second, millis] from the time string.
        final int[] expectedValues = {3, 4, 5, 6};

        // Act
        final int[] actualValues = StringConverter.INSTANCE.getPartialValues(
            partialTemplate, timeString, chronology);

        // Assert
        // Using Arrays.toString provides a clear failure message, which is helpful
        // since JUnit 3's TestCase does not have a dedicated assertArrayEquals method.
        assertEquals(
            "The parsed partial values should match the expected time components.",
            Arrays.toString(expectedValues),
            Arrays.toString(actualValues)
        );
    }
}