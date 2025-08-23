package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Verifies that the constructor throws a NullPointerException when the
     * DateTimeFieldType argument is null.
     * <p>
     * The constructor internally calls {@code fieldType.getName()}, which is the
     * expected cause of the NPE.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_withDateTimeFieldType_shouldThrowNPEForNullType() {
        // Call the constructor with a null DateTimeFieldType to trigger the exception.
        // The other arguments are irrelevant for this specific test case.
        new IllegalFieldValueException(
                (DateTimeFieldType) null,
                (Number) null,
                (Number) null,
                (Number) null,
                (String) null);
    }
}