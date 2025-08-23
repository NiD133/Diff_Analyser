package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests the constructor that accepts a field name and an illegal string value.
     * It verifies that the exception correctly stores the illegal value and
     * formats the exception message as expected.
     */
    @Test
    public void whenConstructedWithIllegalStringValue_thenGettersReturnCorrectInformation() {
        // Arrange: Define a realistic field name and an illegal value.
        // The empty string is used here to match the original test's edge case.
        final String fieldName = "timeZone";
        final String illegalValue = "";

        // Act: Create the exception instance.
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldName, illegalValue);

        // Assert: Verify the state of the created exception.
        // 1. Check that the illegal value is stored and can be retrieved.
        assertEquals("The illegal string value should be stored correctly.",
                illegalValue, exception.getIllegalStringValue());

        // 2. Check that the exception message is formatted as expected.
        final String expectedMessage = "Value \"\" for timeZone is not supported";
        assertEquals("The exception message should be formatted correctly.",
                expectedMessage, exception.getMessage());
    }
}