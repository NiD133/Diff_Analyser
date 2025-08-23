package org.joda.time.convert;

import junit.framework.TestCase;
import org.joda.time.Chronology;

/**
 * Unit tests for {@link StringConverter} focusing on invalid string inputs.
 */
public class StringConverterInvalidInputTest extends TestCase {

    /**
     * Tests that getInstantMillis() throws an IllegalArgumentException when given an empty string,
     * as it cannot be parsed into a date-time.
     */
    public void testGetInstantMillis_withEmptyString_throwsIllegalArgumentException() {
        try {
            StringConverter.INSTANCE.getInstantMillis("", null);
            fail("Expected an IllegalArgumentException to be thrown for an empty string.");
        } catch (IllegalArgumentException expected) {
            // Test passes: the expected exception was thrown.
        }
    }

    /**
     * Tests that getInstantMillis() throws an IllegalArgumentException when given a string
     * that does not conform to the expected ISO 8601 format.
     */
    public void testGetInstantMillis_withInvalidFormatString_throwsIllegalArgumentException() {
        try {
            // The string "X" is not a valid ISO date-time representation.
            StringConverter.INSTANCE.getInstantMillis("X", null);
            fail("Expected an IllegalArgumentException to be thrown for an invalid format string.");
        } catch (IllegalArgumentException expected) {
            // Test passes: the expected exception was thrown.
        }
    }
}