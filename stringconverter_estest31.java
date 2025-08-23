package org.joda.time.convert;

import org.junit.Test;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that getDurationMillis throws a NumberFormatException when parsing an
     * ISO 8601 period string that is missing a value for a field (e.g., "PT-S").
     * The underlying parser attempts to parse an empty string as a number, which fails.
     */
    @Test(expected = NumberFormatException.class)
    public void getDurationMillis_throwsNumberFormatException_forPeriodStringWithMissingValue() {
        // Arrange
        StringConverter converter = StringConverter.INSTANCE;
        // The format "Pt-s" is invalid because the value for seconds 's' is missing.
        String malformedPeriodString = "Pt-s";

        // Act & Assert
        converter.getDurationMillis(malformedPeriodString);
    }
}