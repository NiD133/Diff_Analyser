package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    @Test
    public void getDurationMillis_shouldParsePeriodStringWithTrailingDecimalPoint() {
        // Arrange
        StringConverter converter = new StringConverter();
        // The Joda-Time parser is lenient and handles this non-standard ISO 8601 format.
        // "Pt2.s" is interpreted as a duration of 2 seconds.
        String periodString = "Pt2.s";
        long expectedMillis = 2000L;

        // Act
        long actualMillis = converter.getDurationMillis(periodString);

        // Assert
        assertEquals(expectedMillis, actualMillis);
    }
}