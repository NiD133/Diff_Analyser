package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests that the constructor throws a NullPointerException when the
     * DurationFieldType is null. The constructor attempts to access the name
     * of the field type, which is not possible on a null reference.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_withNullDurationFieldType_throwsNullPointerException() {
        // Call the constructor with a null DurationFieldType, which is expected to fail.
        // The string value provided is arbitrary and does not affect this test's outcome.
        new IllegalFieldValueException((DurationFieldType) null, "some illegal value");
    }
}