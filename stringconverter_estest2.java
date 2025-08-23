package org.joda.time.convert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final StringConverter converter = new StringConverter();

    /**
     * Tests that getDurationMillis() throws an IllegalArgumentException when provided
     * with a string that does not conform to the ISO 8601 duration format.
     */
    @Test
    public void getDurationMillis_shouldThrowException_forInvalidDurationFormat() {
        // Arrange: An invalid duration string that does not follow the ISO format (e.g., "PT1H").
        final String invalidDurationString = "Pt! 8CF";

        // Assert: Expect an IllegalArgumentException with a specific message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid format: \"" + invalidDurationString + "\"");

        // Act: Attempt to convert the invalid string to a duration.
        converter.getDurationMillis(invalidDurationString);
    }
}