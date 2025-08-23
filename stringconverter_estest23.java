package org.joda.time.convert;

import org.joda.time.MutableInterval;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link StringConverter}, focusing on how it handles invalid string formats
 * when parsing Joda-Time objects.
 */
public class StringConverterTest {

    // The ExpectedException rule is a clean, declarative way to test for exceptions in JUnit 4.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that attempting to parse an interval from a string with an invalid format
     * results in an IllegalArgumentException. This test indirectly validates the behavior
     * of StringConverter, which is used internally by MutableInterval.parse().
     */
    @Test
    public void parseInterval_withInvalidFormatString_shouldThrowIllegalArgumentException() {
        // Arrange: Define an input string that does not conform to the expected ISO 8601 interval format.
        final String invalidIntervalString = ";Xb='|Z!0*'jzM0/";

        // Assert: Configure the rule to expect an IllegalArgumentException with a specific message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Format invalid: " + invalidIntervalString);

        // Act: Attempt to parse the invalid string, which should trigger the expected exception.
        MutableInterval.parse(invalidIntervalString);
    }
}