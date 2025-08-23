package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that getDurationMillis() correctly parses an ISO 8601 period string
     * representing a duration of seconds.
     */
    @Test
    public void getDurationMillis_shouldParseISOPeriodStringWithSeconds() {
        // Arrange
        final StringConverter converter = new StringConverter();
        final String isoPeriodString = "Pt2s"; // ISO 8601 format for a 2-second period
        final long expectedMillis = 2000L;

        // Act
        final long actualMillis = converter.getDurationMillis(isoPeriodString);

        // Assert
        assertEquals(expectedMillis, actualMillis);
    }
}